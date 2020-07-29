package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDetails   {
  @JsonProperty("universalId")
  private UUID universalId;

  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("lastName")
  private String lastName;

  @JsonProperty("middleName")
  private String middleName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("accounts")
  private List<Account> accounts = null;

  public UserDetails universalId(UUID universalId) {
    this.universalId = universalId;
    return this;
  }

  public UUID getUniversalId() {
    return universalId;
  }

  public void setUniversalId(UUID universalId) {
    this.universalId = universalId;
  }

  public UserDetails firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UserDetails lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }



  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UserDetails middleName(String middleName) {
    this.middleName = middleName;
    return this;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public UserDetails email(String email) {
    this.email = email;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDetails accounts(List<Account> accounts) {
    this.accounts = accounts;
    return this;
  }

  public UserDetails addAccountsItem(Account accountsItem) {
    if (this.accounts == null) {
      this.accounts = new ArrayList<Account>();
    }
    this.accounts.add(accountsItem);
    return this;
  }


  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

}

