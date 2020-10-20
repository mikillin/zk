package fhdo;

import java.util.List;

public interface FragebogenService {

	/**
	 * Retrieve all surveys in the catalog.
	 * @return all surveys
	 */

	public List<Survey> findAllFrageboegen();

	/**
	 * search surveys  according to keyword in model and make.
	 * @param keyword for search
	 * @return list of surveys that match the keyword
	 */
	public List<Survey> searchFragebogen(String keyword);
}
