package ca.bc.gov.open.jag.efilingaccountclient;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.*;
import ca.bc.gov.open.jag.efilingaccountclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bceid.webservices.client.v9.*;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private final AccountFacadeBean accountFacadeBean;
    private final RoleRegistryPortType roleRegistryPortType;
    private final BCeIDServiceSoap bCeIDServiceSoap;
    private final AccountDetailsMapper accountDetailsMapper;

    private static final Map<String, BCeIDAccountTypeCode> accountTypeLookup;
    static {
        Map<String, BCeIDAccountTypeCode> tempMap = new HashMap<String, BCeIDAccountTypeCode>();
        tempMap.put("business", BCeIDAccountTypeCode.BUSINESS);
        tempMap.put("individual", BCeIDAccountTypeCode.INDIVIDUAL);
        tempMap.put("verified individual", BCeIDAccountTypeCode.VERIFIED_INDIVIDUAL);
        tempMap.put("eds", BCeIDAccountTypeCode.EDS);
        tempMap.put("internal", BCeIDAccountTypeCode.INTERNAL);
        tempMap.put("ldb", BCeIDAccountTypeCode.LDB);
        tempMap.put("ths", BCeIDAccountTypeCode.THS);

        accountTypeLookup = Collections.unmodifiableMap(tempMap);
    }

    public CsoAccountServiceImpl(AccountFacadeBean accountFacadeBean,
                                 RoleRegistryPortType roleRegistryPortType,
                                 BCeIDServiceSoap bCeIDServiceSoap,
                                 AccountDetailsMapper accountDetailsMapper) {

        this.accountFacadeBean = accountFacadeBean;
        this.roleRegistryPortType = roleRegistryPortType;
        this.bCeIDServiceSoap = bCeIDServiceSoap;
        this.accountDetailsMapper = accountDetailsMapper;
    }

    @Override
    public AccountDetails getAccountDetails(UUID userGuid, String bceidAccountType) {

        AccountDetails accountDetails = getCsoDetails(userGuid);
        if (null == accountDetails) {
            accountDetails = getBCeIDDetails(userGuid, bceidAccountType);
        }

        return accountDetails;
    }

    @Override
    public AccountDetails createAccount(CreateAccountRequest createAccountRequest)  {

        // Validate the incoming data
        if (StringUtils.isEmpty(createAccountRequest.getFirstName()) ||
                StringUtils.isEmpty(createAccountRequest.getLastName()) ||
                StringUtils.isEmpty(createAccountRequest.getEmail()) ||
                StringUtils.isEmpty(createAccountRequest.getUniversalId().toString())) {
            throw new IllegalArgumentException("First Name, Last Name, Email, and Universal ID are required");
        }

        AccountDetails accountDetails = null;

        try {

            Account account = setCreateAccountDetails(createAccountRequest);
            Client client = setCreateAccountClientDetails(createAccountRequest);
            List<RoleAssignment> roles = setCreateAccountRoles();
            ClientProfile clientProfile = accountFacadeBean.createAccount(account, client, roles);
            if (null != clientProfile) {
                accountDetails = accountDetailsMapper.toAccountDetails(
                        createAccountRequest.getUniversalId(),
                        clientProfile,
                        hasFileRole(CsoHelpers.formatUserGuid(createAccountRequest.getUniversalId())));
            }
        }
        catch (DatatypeConfigurationException | NestedEjbException_Exception e) {
            throw new EfilingAccountServiceException("Exception while creating CSO account", e.getCause());
        }

        return accountDetails;
    }

    private AccountDetails getCsoDetails(UUID userGuid)  {

        AccountDetails accountDetails = null;
        List<ClientProfile> profiles = new ArrayList<>();
        try {
            profiles.addAll(accountFacadeBean.findProfiles(CsoHelpers.formatUserGuid(userGuid)));
        } catch (NestedEjbException_Exception e) {
            throw new EfilingAccountServiceException("Exception while fetching account details", e);
        }
        // An account must have only one profile associated with it to proceed
        if (profiles.size() == 1) {
            accountDetails = accountDetailsMapper.toAccountDetails(userGuid, profiles.get(0), hasFileRole(CsoHelpers.formatUserGuid(userGuid)));
        }
        else if (profiles.size() > 1) {
            throw new CSOHasMultipleAccountException(profiles.get(0).getClientId().toString());
        }

        return accountDetails;
    }

    private AccountDetails getBCeIDDetails(UUID userGuid, String accountType) {

        AccountDetails accountDetails = null;
        BCeIDAccountTypeCode accountTypeCode = getBCeIDAccountType(accountType);

        if (accountTypeCode != BCeIDAccountTypeCode.VOID) {

            AccountDetailRequest request = new AccountDetailRequest();
            request.setOnlineServiceId("62B2-5550-4376-4DA7");
            request.setRequesterUserGuid(CsoHelpers.formatUserGuid(userGuid));
            request.setRequesterAccountTypeCode(accountTypeCode);
            request.setUserGuid(CsoHelpers.formatUserGuid(userGuid));
            request.setAccountTypeCode(accountTypeCode);
            AccountDetailResponse response = bCeIDServiceSoap.getAccountDetail(request);

            if (response.getCode() == ResponseCode.SUCCESS) {
                accountDetails = accountDetailsMapper.toAccountDetails(userGuid, response.getAccount());
            }
        }

        return accountDetails;
    }

    public boolean hasFileRole(String userGuid) {

        UserRoles userRoles = roleRegistryPortType.getRolesForIdentifier("Courts", "CSO", userGuid, "CAP");
        List<RegisteredRole> roles = userRoles.getRoles();
        return roles != null && roles.stream().anyMatch(r -> r.getCode().equals("FILE"));
    }

    private BCeIDAccountTypeCode getBCeIDAccountType(String bceidAccountType) {

        String lookUp = bceidAccountType.toLowerCase();
        BCeIDAccountTypeCode code = accountTypeLookup.get(lookUp);
        return code == null? BCeIDAccountTypeCode.VOID : code;
    }

    private Account setCreateAccountDetails(CreateAccountRequest createAccountRequest) throws DatatypeConfigurationException {

        Account account = new Account();
        String accountName = createAccountRequest.getFirstName() + " " + createAccountRequest.getLastName();

        account.setAccountNm(accountName);
        account.setAccountPrefixTxt("SA");
        account.setAccountStatusCd("ACT");
        account.setAuthenticatedAccountGuid(CsoHelpers.formatUserGuid(createAccountRequest.getUniversalId()));
        account.setEmailTxt(createAccountRequest.getEmail());
        account.setEntDtm(CsoHelpers.date2XMLGregorian(new Date()));
        account.setFeeExemptYnBoolean(false);
        account.setRegisteredCreditCardYnBoolean(false);

        return account;
    }

    private Client setCreateAccountClientDetails(CreateAccountRequest createAccountRequest) throws DatatypeConfigurationException {
        Client client = new Client();
        XMLGregorianCalendar date =  CsoHelpers.date2XMLGregorian(new Date());

        client.setAuthenticatedClientGuid(CsoHelpers.formatUserGuid(createAccountRequest.getUniversalId()));
        client.setClientPrefixTxt("CS");
        client.setClientStatusCd("ACT");
        client.setEmailTxt(createAccountRequest.getEmail());
        client.setEntDtm(date);
        client.setGivenNm(createAccountRequest.getFirstName());
        client.setMiddleNm(createAccountRequest.getMiddleName());
        client.setRegisteredCreditCardYnBoolean(false);
        client.setServiceConditionsAcceptDtm(date);
        client.setSurnameNm(createAccountRequest.getLastName());

        return client;
    }

    private List<RoleAssignment> setCreateAccountRoles() {

        List<RoleAssignment> roles = new ArrayList<>();
        RoleAssignment role = new RoleAssignment();
        role.setActiveYn(true);

        role.setRegisteredClientRoleCd("IND");
        roles.add(role);

        role.setRegisteredClientRoleCd("CAEF");
        roles.add(role);

        role.setRegisteredClientRoleCd("FILE");
        roles.add(role);

        return roles;
    }
}
