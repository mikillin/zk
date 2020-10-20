package fhdo;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.*;
import fh.TreeElement;

import java.util.ArrayList;
import java.util.List;

public class TreeDynamicVM {


    private Tree zkTree = new Tree();
    private TreeNode root;
    private DefaultTreeModel treeModel; // bind to tree in zul

    @Command
    public void select() {
        //todo: implement
        System.out.println("");
    }

    @Init
    public void init() {

        ArrayList<TreeElement> entryList = new ArrayList<>();
        entryList.add(new TreeElement(1, "name 1"));
        entryList.add(new TreeElement(2, "name 2"));
        entryList.add(new TreeElement(3, "name 3"));
        entryList.add(new TreeElement(4, "name 4"));
        TreeElement te = new TreeElement(5, "name 5");
        te.setText("instead of name 5");
        TreeElement te2 = new TreeElement(51, "name 51");
        te.setText("instead of name 51");
        ArrayList<TreeElement> subentries = new ArrayList<TreeElement>();
        subentries.add(te2);
        te.setSubEntries(subentries);
        entryList.add(te);
        createTree(entryList);

    }


    private void createTree(List<TreeElement> dataList) {
        root = new DefaultTreeNode(null, createTreeNodeList(dataList));
        treeModel = new DefaultTreeModel(root);
        treeModel.setMultiple(true);
        zkTree.setModel(treeModel);

//        if (treeRenderer == null) {
//            treeRenderer = new TreeRenderer(this);
//            zkTree.setItemRenderer(treeRenderer);
//        }
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

    public Tree getTree() {
        return zkTree;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }
}
