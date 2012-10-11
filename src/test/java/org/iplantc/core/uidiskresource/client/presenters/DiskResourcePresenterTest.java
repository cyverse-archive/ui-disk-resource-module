/**
 * 
 */
package org.iplantc.core.uidiskresource.client.presenters;

import java.util.ArrayList;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.loader.TreeLoader;



/**
 * @author jstroot
 *
 */
@RunWith(JMock.class)
public class DiskResourcePresenterTest {// extends TestCase {

    public Mockery context = new JUnit4Mockery();
    private DiskResourceViewToolbar toolbar;
    private DiskResourceView view;
    private DiskResourceView.Proxy proxy;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        view = context.mock(DiskResourceView.class);
        toolbar = context.mock(DiskResourceViewToolbar.class);
        proxy = context.mock(DiskResourceView.Proxy.class);

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {}

    /**
     * Test method for {@link DiskResourceView.Presenter#onFolderSelected(Folder)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnFolderSelected() {

        final Folder folder = context.mock(Folder.class);
        context.checking(new Expectations() {{
                oneOf(view).getTreeStore();
                oneOf(view).setTreeLoader(with(aNonNull(TreeLoader.class)));
                oneOf(view).setPresenter(with(aNonNull(DiskResourceView.Presenter.class)));
                oneOf(proxy).setPresenter(with(aNonNull(DiskResourceView.Presenter.class)));
                oneOf(view).setNorthWidget(with(aNonNull(IsWidget.class)));
                oneOf(toolbar).setPresenter(with(aNonNull(DiskResourceViewToolbar.Presenter.class)));
            oneOf (proxy).load(folder);
        }});
        DiskResourceView.Presenter presenter = new DiskResourcePresenter(view, toolbar, proxy);
        
        presenter.onFolderSelected(folder);

        context.assertIsSatisfied();
    }

    /**
     * This tests the case when {@link DiskResourceView.Presenter#onFolderLoad(Folder, ArrayList)} is
     * called with a
     * folder which <em>is not equal</em> to the currently selected folder.
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnFolderLoadWithNonMatchingSelectedFolder() {

        final Folder folder = context.mock(Folder.class, "inputFolder");
        final Folder retFolder = context.mock(Folder.class, "retFolder");
        context.checking(new Expectations() {
            {
                oneOf(view).getTreeStore();
                oneOf(view).setTreeLoader(with(aNonNull(TreeLoader.class)));
                oneOf(view).setPresenter(with(aNonNull(DiskResourceView.Presenter.class)));
                oneOf(proxy).setPresenter(with(aNonNull(DiskResourceView.Presenter.class)));
                oneOf(view).setNorthWidget(with(aNonNull(IsWidget.class)));
                oneOf(toolbar).setPresenter(with(aNonNull(DiskResourceViewToolbar.Presenter.class)));
                atLeast(1).of(view).getSelectedFolder();
                will(returnValue(retFolder));
            }
        });
        DiskResourcePresenter presenter = new DiskResourcePresenter(view, toolbar, proxy);

        presenter.onFolderLoad(folder, null);

        context.assertIsSatisfied();
    }

    /**
     * This tests the case when {@link DiskResourceView.Presenter#onFolderLoad(Folder, ArrayList)} is
     * called with a
     * folder which <em>is equal</em> to the currently selected folder.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnFolderLoadWithMatchingSelectedFolder() {

        final Folder folder = context.mock(Folder.class);
        final ArrayList<DiskResource> folderChildren = Lists.newArrayList();
        context.checking(new Expectations() {
            {
                oneOf(view).getTreeStore();
                oneOf(view).setTreeLoader(with(aNonNull(TreeLoader.class)));
                oneOf(view).setPresenter(with(aNonNull(DiskResourceView.Presenter.class)));
                oneOf(proxy).setPresenter(with(aNonNull(DiskResourceView.Presenter.class)));
                oneOf(view).setNorthWidget(with(aNonNull(IsWidget.class)));
                oneOf(toolbar).setPresenter(with(aNonNull(DiskResourceViewToolbar.Presenter.class)));
                atLeast(1).of(view).getSelectedFolder();
                will(returnValue(folder));
                oneOf(view).setDiskResources(with(folderChildren));
            }
        });
        DiskResourcePresenter presenter = new DiskResourcePresenter(view, toolbar, proxy);

        presenter.onFolderLoad(folder, folderChildren);

        context.assertIsSatisfied();
    }


    public void testNext() {

    }
}
