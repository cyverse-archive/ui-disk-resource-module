/**
 * 
 */
package org.iplantc.core.uidiskresource.client.search.models;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 *
 */
public interface DataSearchResult {

    @PropertyName("hits")
    List<DataSearch> getSearchResults();
}
