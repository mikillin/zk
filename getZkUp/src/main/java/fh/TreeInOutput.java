package fh;

//import de.fhdo.helper.IUpdateModal;

import db.service.TreeSearchService;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.ConventionWires;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeInOutput extends Window implements AfterCompose
{


    public static final int MINIMUM_ENTERED_SYMBOLS_FOR_SEARCHING = 2;

    Tree mainTree;
    TreeNode root;
    private ArrayList<TreeElement> entryList;
    private TreeFilter filter;
    private DefaultTreeModel treeModel;
    private TreeRenderer treeRenderer;

    private void createTree(List<TreeElement> dataList) {
        root = new DefaultTreeNode(null, createTreeNodeList(dataList));
        treeModel = new DefaultTreeModel(root);
        mainTree.setModel(treeModel);

        if (treeRenderer == null) {
            treeRenderer = new TreeRenderer(null);
            mainTree.setItemRenderer(treeRenderer);
        }
    }

    private List<TreeNode> createTreeNodeList(List<TreeElement> dataList) {
        List<TreeNode> list = new ArrayList<>();

        for (TreeElement entry : dataList) {
            if (entry.getSubEntries() == null || entry.getSubEntries().size() == 0) {
                list.add(new DefaultTreeNode(entry));
            } else {
                list.add(new DefaultTreeNode(entry, createTreeNodeList(entry.getSubEntries())));
            }
        }
        return list;
    }

    public void onChangeFilter(InputEvent event) {
        if (filter == null) {
            filter = new TreeFilter();
        }

        String filterString = !event.getValue().isEmpty() ? event.getValue() : null;
        if (filterString != null && filterString.length() >= MINIMUM_ENTERED_SYMBOLS_FOR_SEARCHING) {
            filter.setText(filterString);
            filterTree();
        }
    }

    private boolean checkFilter(TreeElement entry) {

        if (filter.getText() != null && filter.getText().length() >= MINIMUM_ENTERED_SYMBOLS_FOR_SEARCHING) {
            return entry.getText().toLowerCase().contains(filter.getText().toLowerCase());
        }
        return true;
    }

    private void filterTree() {
        reset();
        for (TreeElement entry : entryList) {
            filterTreeRecursive(null, entry);
        }

        mainTree.setModel(treeModel);
    }

    private void filterTreeRecursive(TreeElement parent, TreeElement child) {
        if (!child.isVisited()) {
            child.setVisible(false);
            child.setVisited(true);
        }
        if (checkFilter(child)) {
            child.setVisible(true);
        } else {
            child.setVisible(false);
        }
        if (child.getSubEntries() != null && child.getSubEntries().size() > 0) {
            for (TreeElement grandChild : child.getSubEntries()) {
                filterTreeRecursive(child, grandChild);
            }
        }
        if (parent != null && child.isVisible()) {
            parent.setVisible(true);
        }

    }

    public ArrayList<TreeElement> getEntryList() {
        return entryList;
    }

    public void setEntryList(ArrayList<TreeElement> entryList) {
        this.entryList = entryList;
        init();
        createTree(this.entryList);
    }

    @Override
    public void afterCompose() {
        ConventionWires.wireVariables(this, this);

        TreeElement category = null, question;
        ArrayList<TreeElement> entryList = new ArrayList<>();
        ArrayList<TreeElement> subEntries;

        entryList.clear();
        List<Object[]> assessmentEntities = new TreeSearchService().getCategoriesAndQuestions();

        int catId = -1;
        int questionId = -1;
        long questionFullId = -1; // catId * 100 + questionId;
        subEntries = new ArrayList<>();

        for (Object[] assessmentEntityObject : assessmentEntities) {
            questionFullId = ((Integer) assessmentEntityObject[0]).longValue() * 100 +((Integer) assessmentEntityObject[1]).longValue();
            if (catId != (int) assessmentEntityObject[0]) {
                catId = (int) assessmentEntityObject[0];
                // add old
                if (subEntries.size() > 0) {
                    if (category == null)
                        category = new TreeElement(((Integer) assessmentEntityObject[0]).longValue() * 100, (String) assessmentEntityObject[2], true);
                    category.setSubEntries(subEntries);
                    entryList.add(category);
                }
                category = new TreeElement(((Integer) assessmentEntityObject[0]).longValue() * 100, (String) assessmentEntityObject[2], true);
                subEntries = new ArrayList<>();
            }

            question = new TreeElement(questionFullId, (String) assessmentEntityObject[3]);
            subEntries.add(question);
        }

        if (subEntries.size() > 0) { //last category
            category.setSubEntries(subEntries);
            entryList.add(category);
        }


        setEntryList(entryList);
    }

    public void onCancelClicked() {
        this.detach();
        setVisible(false);
    }

    public void onOkClicked() {
        Map<String, Object> data = new HashMap<String, Object>();
        Object value = null;
        if (mainTree.getSelectedItem() != null) {
            value = mainTree.getSelectedItem().getValue();
            data.put("data", ((TreeElement) value).getId());
        }
        this.detach();

        BindUtils.postGlobalCommand(null, null, "sendActivity", data);
        setVisible(false);
    }



    private void reset() {
        for (TreeElement entry : entryList) {
            resetRecursivly(entry);
        }
    }

    private void resetRecursivly(TreeElement entry) {
        entry.setVisited(false);
        if (entry.getSubEntries() != null && !entry.getSubEntries().isEmpty()) {
            for (TreeElement child : entry.getSubEntries()) {
                resetRecursivly(child);
            }
        }
    }

    private void init() {
        for (TreeElement entry : entryList) {
            initRecursivly(entry);
        }
    }

    private void initRecursivly(TreeElement entry) {
        entry.setVisited(false);
        entry.setVisible(true);
        if (entry.getSubEntries() != null && !entry.getSubEntries().isEmpty()) {
            for (TreeElement child : entry.getSubEntries()) {
                resetRecursivly(child);
            }
        }
    }

    public Tree getTree() {
        return mainTree;
    }
}