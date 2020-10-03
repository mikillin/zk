package fh;


import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;


public class TreeRenderer implements TreeitemRenderer {
    private IDoubleClick doubleClickEvent;

    public TreeRenderer(IDoubleClick doubleClickEvent) {
        this.doubleClickEvent = doubleClickEvent;
    }

    @Override
    public void render(Treeitem treeitem, Object o, int i) {
        if (o instanceof DefaultTreeNode && ((DefaultTreeNode) o).getData() instanceof TreeElement) {
            TreeElement entry = (TreeElement) ((DefaultTreeNode) o).getData();

            treeitem.setVisible(entry.isVisible());
            treeitem.setValue(entry);
            treeitem.setOpen(true);
            treeitem.setSelected(entry.isSelected());

            Treerow row = new Treerow();
            Treecell cell = new Treecell();
            String description = entry.getText();
            cell.setLabel(description);
            row.appendChild(cell);
            row.setStyle("height: 30px;");
            treeitem.appendChild(row);

//            if (treeitem.getEventListeners(Events.ON_DOUBLE_CLICK) == null || !treeitem.getEventListeners(Events.ON_DOUBLE_CLICK).iterator().hasNext()) {
//                treeitem.addEventListener(Events.ON_DOUBLE_CLICK, event -> {
//                    if (doubleClickEvent != null) {
//                        doubleClickEvent.onDoubleClick(((Treeitem) event.getTarget()).getValue());
//                    }
//                });
//            }
        }
    }
}
