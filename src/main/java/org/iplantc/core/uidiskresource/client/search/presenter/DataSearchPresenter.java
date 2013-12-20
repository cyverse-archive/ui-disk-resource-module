package org.iplantc.core.uidiskresource.client.search.presenter;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

/**
 * An interface definition for the "search" sub-system.
 * 
 * <h2><u>Terms and Concepts</u></h2>
 * <dl>
 * <dt>Query Template</dt>
 * <dd>a template which is used to generate a query to be submitted to the search endpoints.</dd>
 * <dd>acts as a "smart folder" which is accessed from the data navigation window.</dd>
 * <dt>Active Query</dt>
 * <dd>the current query template whose generated search query results are displayed in the view's center
 * panel.</dd>
 * <dd>
 * </dl>
 * 
 * <h2>Presenter Responsibilities</h2>
 * <ul>
 * <li>Managing the <em>active query</em> state. This includes:
 * <ul>
 * <li>Ensuring that the view communicates to the user what the current <em>active query</em> is.</li>
 * <li>Ensuring that the view communicates to the user if there is no <em>active query</em>.</li>
 * </ul>
 * </li>
 * 
 * <li>Maintaining a list of saved query templates.</li>
 * <li>Saving query templates when the user requests.<br/>
 * This includes ensuring that the user has full permissions to the query template.</li>
 * <li>Retrieving saved query templates
 * <ul>
 * <li>Displaying the saved filters as selectable root items in the Navigation panel</li>
 * </ul>
 * </li>
 * 
 * <li>Using and/or applying the <em>active query</em> template to submit searches to the
 * {@link SearchServiceFacade#submitSearchFromQueryTemplate()} method.<br/>
 * &nbsp; This can occur as a result of the user selecting a query in the view.</li>
 * <li>Ensure the results of the constructed query are displayed to the user in the view.</li>
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
 * <li>Selection events from the "Navigation Panel", when the "Magic Folders" are clicked. These
 * selection events are synonymous with {@link SubmitDiskResourceQueryEvent} for the selected query
 * template.
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
 * &nbsp;<i><b>IF</b> we find a way to conveniently display non-saved filters, active filter can be
 * easily communicated to the user.</i></li>
 * 
 * <li>Who is responsible for <em>"styling"</em> the "magic folders"? Presenter or view?</li>
 * <li>How do we actually compose these different presenters?<br/>
 * &nbsp; this could be done by injecting the sub-presenters into the {@link DiskResourceView.Presenter}
 * at construction. We would still have to give this presenter reference to the items it needs to perform
 * its duties. That, or give this sub-presenter a reference to the primary presenter, which will provide
 * the necessary references.<br/>
 * &nbsp; Is there any other way? Is this proposal sufficient?</li>
 * </ul>
 * 
 * @author jstroot
 * 
 */
public interface DataSearchPresenter extends SaveDiskResourceQueryEventHandler, SubmitDiskResourceQueryEventHandler {

    /**
     * Initializes this presenter and its corresponding view for search operations.
     * 
     * Retrieve any saved query templates, adds itself as a listener for
     * {@code SubmitDiskResourceQueryEvent} and {@code SaveDiskResourceQueryEvent}s on the given view's
     * toolbar, and {@code SubmitDiskResourceQueryEvent}s on the view itself.
     * 
     * @param view
     */
    void searchInit(DiskResourceView view);

    /**
     * Returns the view associated with this presenter.
     * 
     * This is typically set via {@link #searchInit(DiskResourceView)}.
     * 
     * @return the view associated with this presenter.
     */
    DiskResourceView getView();

    /**
     *
     * @return the current active query, or null if there is not active query.
     */
    DiskResourceQueryTemplate getActiveQuery();

}
