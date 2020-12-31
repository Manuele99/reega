package reega.data;

import java.sql.SQLException;

import reega.users.GenericUser;
import reega.users.NewUser;

public interface DataController {
	
	public void addUser(NewUser newUser) throws SQLException;

	/**
	 * Login using email and password
	 * 
	 * @param email
	 * @param hash  the password's hash generated by AES.encrypt method
	 * @return the user if login succeeded or null if email not found or wrong
	 *         password
	 * @throws SQLException 
	 * @See GenericUser
	 */
	public GenericUser emailLogin(String email, String hash) throws SQLException;

	/**
	 * Login using fiscal code and password
	 * 
	 * @param fiscalCode ths user's fiscal code
	 * @param hash       the password's hash generated by AES.encrypt method
	 * @return the user if login succeeded or null if fiscal code not found or wrong
	 *         password
	 * @throws SQLException 
	 */
	public GenericUser fiscalCodeLogin(String fiscalCode, String hash) throws SQLException;

	void kill() throws SQLException;
}
