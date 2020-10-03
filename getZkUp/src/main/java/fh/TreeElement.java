package fh;


import java.util.List;

public class TreeElement implements Comparable<TreeElement> {

    private long id;

    private String text;
    private boolean category;
    private boolean isVisible = true;
    private boolean selected;

    private List<TreeElement> subEntries;

    private boolean visited;

    public TreeElement(long id, String text, boolean category) {
        this.id = id;
        this.category = category;
        this.text = text;
    }

    public TreeElement(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public List<TreeElement> getSubEntries() {
        return subEntries;
    }

    public void setSubEntries(List<TreeElement> subEntries) {
        this.subEntries = subEntries;

    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }

    @Override
    public int compareTo(TreeElement o) {
        return Long.compare(id, o.getId());
    }
}
