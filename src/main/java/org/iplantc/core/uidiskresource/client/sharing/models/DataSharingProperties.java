/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.models;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 *
 */
public interface DataSharingProperties extends PropertyAccess<DataSharing> {
	
	ValueProvider<DataSharing, String> name();
	
	ValueProvider<DataSharing, String> displayPermission();
}
