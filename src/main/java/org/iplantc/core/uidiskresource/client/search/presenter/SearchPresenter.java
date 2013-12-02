package org.iplantc.core.uidiskresource.client.search.presenter;

import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;

/**
 * An interface definition for the "search" sub-system.
 * 
 * <h2><u>Terms and Concepts</u></h2>
 * <dl>
 * <dt>Active Query</dt>
 * <dd>The current query template whose generated search query results are displayed in the view's center
 * panel.</dd>
 * <dd>
 * </dl>
 * 
 * <h2>Presenter Responsibilities</h2>
 * <ul>
 * <li>Managing the <em>active query</em> state.</li>
 * <li>Ensuring that the "active filter" is clearly displayed to the user.</li>
 * <li>Performing searches (a.k.a. applying the filter) and displaying results</li>
 * 
 * <li>Retrieving saved filters
 * <ul>
 * <li>Displaying the saved filters as selectable root items in the Navigation panel</li>
 * </ul>
 * </li>
 * 
 * </ul>
 * 
 * <h2>What does this presenter need at construction-time?</h2>
 * <ul>
 * <li>The search box</li>
 * <li>Navigation panel</li>
 * <li>Center panel<br/>
 * <i>For displaying results</i></li>
 * </ul>
 * 
 * <h2>What initialization-time tasks does this presenter perform?</h2>
 * <ul>
 * <li>Fetching a user's saved 'filters' and putting them into the Navigation panel</li>
 * </ul>
 * 
 * <h2>What user interactions does this presenter react to?</h2>
 * <ul>
 * <li>{@link SaveDiskResourceQueryEvent}</li>
 * <li>{@link SubmitDiskResourceQueryEvent}</li>
 * 
 * <li>Selection events from the "Navigation Panel", when the "Magic Folders" are clicked
 * <ul>
 * <li>When a "magic folder" is selected, the search box should be updated</li>
 * </ul>
 * </li>
 * 
 * </ul>
 * 
 * <h3>Design Questions</h3>
 * <ul>
 * <li>Do we need to track the currently selected filter?<br/>
 * <i>A selected filter <u><b>IS</b></u> the active filter</i><br/>
 * <i><b>IF</b> we find a way to conveniently display non-saved filters, active filter can be easily
 * communicated to the user.</i></li>
 * 
 * <li>Who is responsible for <em>"styling"</em> the "magic folders"? Presenter or view?</li>
 * </ul>
 * 
 * @author jstroot
 * 
 */
public interface SearchPresenter {

    /**
     * Initializes this presenter and its corresponding view for search operations.
     */
    void searchInit();

}
