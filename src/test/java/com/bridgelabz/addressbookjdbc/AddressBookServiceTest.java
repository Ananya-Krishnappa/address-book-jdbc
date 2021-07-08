package com.bridgelabz.addressbookjdbc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bridgelabz.addressbookjdbc.dao.AddressBookRepository;
import com.bridgelabz.addressbookjdbc.dto.AddressBook;
import com.bridgelabz.addressbookjdbc.exception.AddressBookException;
import com.bridgelabz.addressbookjdbc.service.impl.AddressBookService;

@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {

	@InjectMocks
	private AddressBookService mockAddressBookService;

	private AddressBookService addressBookService;

	@Mock
	AddressBookRepository mockAddressBookRepository;

	@BeforeEach
	public void initialize() {
		mockAddressBookService = new AddressBookService(mockAddressBookRepository);
		addressBookService = new AddressBookService();
	}

	@Test
	public void givenAContact_whenCalledAdd_shouldCreateContactInAddressBook() throws AddressBookException {
		AddressBook addressBook = new AddressBook();
		addressBook.setFirstName("leo");
		addressBook.setLastName("Jack");
		addressBook.setAddress("qatar");
		addressBook.setCity("jaipur");
		addressBook.setPhoneNumber("09877654");
		addressBook.setState("rajasthan");
		addressBook.setZip("123");
		addressBook.setEmail("apc@123.com");
		AddressBook newAddressBook = new AddressBook(1, addressBook.getFirstName(), addressBook.getLastName(),
				addressBook.getAddress(), addressBook.getCity(), addressBook.getState(), addressBook.getZip(),
				addressBook.getPhoneNumber(), addressBook.getEmail());
		Mockito.when(mockAddressBookRepository.addContactToAddressBook(Mockito.any(AddressBook.class)))
				.thenReturn(newAddressBook);
		AddressBook result = mockAddressBookService.addContactToAddressBook(addressBook);
		assertTrue(result.getId() == 1);
		Mockito.verify(mockAddressBookRepository).addContactToAddressBook(Mockito.any(AddressBook.class));
	}
}
