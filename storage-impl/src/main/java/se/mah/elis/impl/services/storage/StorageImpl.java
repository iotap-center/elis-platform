package se.mah.elis.impl.services.storage;

import se.mah.elis.impl.services.storage.query.SQLJetQueryTranslator;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.data.ElisDataObject;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.storage.result.ResultSet;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.UserIdentifier;

public class StorageImpl implements Storage {
	
	private QueryTranslator translator;

	public StorageImpl() {
		translator = new SQLJetQueryTranslator();
	}

	@Override
	public void insert(ElisDataObject data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(ElisDataObject[] data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(AbstractUser user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(AbstractUser[] user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ElisDataObject data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ElisDataObject[] data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(AbstractUser user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(AbstractUser[] user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ElisDataObject data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ElisDataObject[] data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(AbstractUser user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(AbstractUser[] user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Query query) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public ElisDataObject readData(long id) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractUser readUser(UserIdentifier id) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet select(Query query) throws StorageException {
		ResultSet result = null;
				
		return result;
	}

}
