package ca.bc.gov.open.jag.efilingcsoclient;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.AccountDetailsMapper;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private final AccountFacadeBean accountFacadeBean;
    private final RoleRegistryPortType roleRegistryPortType;
    private final AccountDetailsMapper accountDetailsMapper;

    public CsoAccountServiceImpl(AccountFacadeBean accountFacadeBean,
                                 RoleRegistryPortType roleRegistryPortType,
                                 AccountDetailsMapper accountDetailsMapper) {

        this.accountFacadeBean = accountFacadeBean;
        this.roleRegistryPortType = roleRegistryPortType;
        this.accountDetailsMapper = accountDetailsMapper;

    }

    @Override
    public AccountDetails getAccountDetails(String universalId) {

        AccountDetails accountDetails = getCsoDetails(universalId);
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
        catch (DatatypeConfigurationException | NestedEjbException_Exception | WebServiceException e) {
            throw new EfilingAccountServiceException("Exception while creating CSO account", e.getCause());
        }

        return accountDetails;
    }

    @Override
    public void updateClient(AccountDetails accountDetails) {
        if (accountDetails == null) throw new IllegalArgumentException("account details required");
        if (accountDetails.getClientId() == null) throw new IllegalArgumentException("client id is required");
        if (StringUtils.isBlank(accountDetails.getInternalClientNumber())) throw new IllegalArgumentException("internal client number is required");

        try {
            Client client = accountFacadeBean.getClient(accountDetails.getClientId());

            client.setInternalClientNo(accountDetails.getInternalClientNumber());
            client.setRegisteredCreditCardYnBoolean(accountDetails.isCardRegistered());
            client.setUpdDtm(DateUtils.getCurrentXmlDate());
            client.setUpdUserId(accountDetails.getClientId().toString());

            accountFacadeBean.updateClient(client);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingAccountServiceException("Exception while updating client", e);
        }

    }

    @Override
    public String getOrderNumber() {
        try {
            return accountFacadeBean.getNextOrderNumber().toString();
        } catch (NestedEjbException_Exception e) {
            throw new EfilingAccountServiceException("Exception while fetching next order number", e);
        }
    }

    private AccountDetails getCsoDetails(String universalId)  {

        AccountDetails accountDetails = null;
        List<ClientProfile> profiles = new ArrayList<>();
        try {
            profiles.addAll(accountFacadeBean.findProfiles(CsoHelpers.formatUserGuid(universalId)));
        } catch (NestedEjbException_Exception | WebServiceException e) {
            throw new EfilingAccountServiceException("Exception while fetching account details", e);
        }
        // An account must have only one profile associated with it to proceed
        if (profiles.size() == 1) {
            accountDetails = accountDetailsMapper.toAccountDetails(universalId, profiles.get(0), hasFileRole(CsoHelpers.formatUserGuid(universalId)));
        }
        else if (profiles.size() > 1) {
            throw new CSOHasMultipleAccountException(profiles.get(0).getClientId().toString());
        }

        return accountDetails;
    }

    public boolean hasFileRole(String userGuid) {

        UserRoles userRoles = roleRegistryPortType.getRolesForIdentifier("Courts", "CSO", userGuid, "CAP");
        List<RegisteredRole> roles = userRoles.getRoles();
        return roles != null && roles.stream().anyMatch(r -> r.getCode().equals("FILE"));
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
        client.setAuthoritativePartyId(BigDecimal.ONE);
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
        RoleAssignment roleInd = new RoleAssignment();
        roleInd.setActiveYn(true);
        roleInd.setRegisteredClientRoleCd("IND");
        roles.add(roleInd);

        RoleAssignment roleCaef = new RoleAssignment();
        roleCaef.setActiveYn(true);
        roleCaef.setRegisteredClientRoleCd("CAEF");
        roles.add(roleCaef);

        RoleAssignment roleFile = new RoleAssignment();
        roleFile.setActiveYn(true);
        roleFile.setRegisteredClientRoleCd("FILE");
        roles.add(roleFile);

        return roles;
    }
}
