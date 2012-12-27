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
public interface DataSearch {

    @PropertyName("name")
    void setName(String name);

    @PropertyName("name")
    String getName();
    
    @PropertyName("_index")
    void setIndex(String index);
    
    @PropertyName("_index")
    String getIndex();
    
    @PropertyName("_type")
    String getType();
    
    @PropertyName("_type")
    void setType(String type);
    
    @PropertyName("_id")
    void setId(String id);

    @PropertyName("_id")
    String getId();
    
    @PropertyName("score")
    int getScore();
    
    @PropertyName("score")
    void setScore(int score);
    
    @PropertyName("viewers")
    List<String> getUsers();
    
    @PropertyName("viewers")
    void setUsers(List<String> users);

}
