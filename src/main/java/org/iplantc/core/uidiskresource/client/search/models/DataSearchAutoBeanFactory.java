/**
 * 
 */
package org.iplantc.core.uidiskresource.client.search.models;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author sriram
 *
 */
public interface DataSearchAutoBeanFactory extends AutoBeanFactory {

    AutoBean<DataSearch> dataSearch();

    AutoBean<DataSearchResult> dataSearchResult();
}
