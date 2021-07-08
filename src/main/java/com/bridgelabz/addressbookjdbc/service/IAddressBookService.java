package com.bridgelabz.addressbookjdbc.service;

import com.bridgelabz.addressbookjdbc.dto.AddressBook;
import com.bridgelabz.addressbookjdbc.exception.AddressBookException;

public interface IAddressBookService {
	public AddressBook addContactToAddressBook(AddressBook addressBook) throws AddressBookException;
}
