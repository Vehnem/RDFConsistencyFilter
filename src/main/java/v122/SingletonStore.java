package v122;

import org.apache.jena.rdf.model.Model;

/**
 * Program configuration
 * 
 * @author marvin
 *
 */
public final class SingletonStore implements StoreInterface {

	private static SingletonStore instance = null;
	
	private static StoreInterface storeInstance;

	private SingletonStore() {
	}

	public static synchronized SingletonStore getInstance() {
		if (instance == null) {
			instance = new SingletonStore();
		}
		return instance;
	}

	/**
	 * Use Memory or File system
	 */
	static boolean useMemory = false;

	/**
	 * Init useMemory
	 * 
	 * @param useMemory
	 */
	public static void init(final boolean useMemory) {

		SingletonStore.useMemory = useMemory;

		if (false == useMemory) {
			SingletonStore.storeInstance = SingletonFileStore.getInstance();
		} else {
			SingletonStore.storeInstance = SingletonMemoryStore.getInstance();
		}
	}

	/*
	 * Management
	 */


	@Override
	public String addRDFData(Model model) {
		return SingletonStore.storeInstance.addRDFData(model);
	}

	@Override
	public Model getRDFData(String datakey) {
				return SingletonStore.storeInstance.getRDFData(datakey);
	}

	@Override
	public void deleteRDFData(String datakey) {
		SingletonStore.storeInstance.deleteRDFData(datakey);
	}

	@Override
	public void autoDelete() {
		SingletonStore.storeInstance.autoDelete();
		
	}

	@Override
	public void cleanStore() {
		SingletonStore.storeInstance.cleanStore();
		
	}

	@Override
	public void addRDFDataResult(String datakey, Model model) {
		SingletonStore.storeInstance.addRDFDataResult(datakey, model);
		
	}

	@Override
	public Model getRDFDataResult(String datakey) {
		return SingletonStore.storeInstance.getRDFDataResult(datakey);
	}
}
