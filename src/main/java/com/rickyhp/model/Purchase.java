package com.rickyhp.model;

import java.util.Objects;

public class Purchase {
	private String firstName;
    private String creditCardNumber;
    private String last4D;

  public String getLast4D() {
		return last4D;
	}

	public void setLast4D(String last4d) {
		last4D = last4d;
	}

private Purchase(Builder builder) {
      firstName = builder.firstName;
      creditCardNumber = builder.creditCardNumber;
      last4D = builder.last4D;
  }

  public static Builder builder() {
      return new Builder();
  }

  public static Builder builder(Purchase copy) {
      Builder builder = new Builder();
      builder.firstName = copy.firstName;
      builder.creditCardNumber = copy.creditCardNumber;
      builder.last4D = copy.last4D;
      return builder;
  }


  public String getFirstName() {
      return firstName;
  }

  public String getCreditCardNumber() {
      return creditCardNumber;
  }

  @Override
  public String toString() {
      return "Purchase{" +
              "firstName='" + firstName + '\'' +
              ", creditCardNumber='" + creditCardNumber + '\'' +
              ", last4D='" + last4D + '}';
  }

  public static final class Builder {
      private String firstName;
      private String creditCardNumber;
      private String last4D;
      
      private static final String CC_NUMBER_REPLACEMENT="xxxx-xxxx-xxxx-";

      private Builder() {
      }

      public Builder firstName(String val) {
          firstName = val;
          return this;
      }

      public Builder maskCreditCard(){
          Objects.requireNonNull(this.creditCardNumber, "Credit Card can't be null");
          String last4Digits = this.creditCardNumber.split("-")[3];
          this.creditCardNumber = CC_NUMBER_REPLACEMENT+last4Digits;
          this.last4D = last4Digits;
          return this;
      }

      public Builder creditCardNumber(String val) {
          creditCardNumber = val;
          return this;
      }

      public Builder last4D(String val) {
          last4D = val;
          return this;
      }
      
      public Purchase build() {
          return new Purchase(this);
      }
  }
}
