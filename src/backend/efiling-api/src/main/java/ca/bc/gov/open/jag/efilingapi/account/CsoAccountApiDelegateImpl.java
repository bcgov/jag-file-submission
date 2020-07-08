package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.CsoAccountApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.Account;
import ca.bc.gov.open.jag.efilingapi.api.model.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Suite of services to manage cso account
 */
@Service
public class CsoAccountApiDelegateImpl implements CsoAccountApiDelegate {


    @Override
    public ResponseEntity<UserDetails> createAccount(UserDetails userDetails) {
        Account account = new Account();
        account.setIdentifier("id");
        account.setType(Account.TypeEnum.CSO);
        userDetails.addAccountsItem(account);
        return new ResponseEntity<>(userDetails, HttpStatus.CREATED);
    }

}
