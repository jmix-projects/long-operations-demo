package com.company.demo.screen.customer;

import io.jmix.ui.screen.*;
import com.company.demo.entity.Customer;

@UiController("Customer.browse")
@UiDescriptor("customer-browse.xml")
@LookupComponent("customersTable")
public class CustomerBrowse extends StandardLookup<Customer> {
}