package game.gui;

public interface ComponentLocator {
	public static final ComponentLocator NORTH = new ComponentLocator()
	{
		public void setPosition(Document document, Component component, float relativeX, float relativeY) {
			component.setPosition(document.getX() + document.getWidth() / 2 - component.getWidth() / 2, document.getY() + 32);
		}
		public void preSetPosition(Document document) {}
		public void postSetPosition(Document document) {}
	};
	public static final ComponentLocator SOUTH = new ComponentLocator()
	{
		public void setPosition(Document document, Component component, float relativeX, float relativeY) {
			component.setPosition(document.getX() + document.getWidth() / 2 - component.getWidth() / 2, document.getY() + document.getHeight() - component.getHeight() - 32);
		}
		public void preSetPosition(Document document) {}
		public void postSetPosition(Document document) {}
	};
	public static final ComponentLocator WEST = new ComponentLocator()
	{
		public void setPosition(Document document, Component component, float relativeX, float relativeY) {
			component.setPosition(document.getX() + 32, document.getY() + document.getHeight() / 2 - component.getHeight() / 2);
		}
		public void preSetPosition(Document document) {}
		public void postSetPosition(Document document) {}
	};
	public static final ComponentLocator EAST = new ComponentLocator()
	{
		public void setPosition(Document document, Component component, float relativeX, float relativeY) {
			component.setPosition(document.getX() + document.getWidth() - component.getWidth() - 32, document.getY() + document.getHeight() / 2 - component.getHeight() / 2);
		}
		public void preSetPosition(Document document) {}
		public void postSetPosition(Document document) {}
	};
	public void preSetPosition(Document document);
	public void setPosition(Document document, Component component, float relativeX, float relativeY);
	public void postSetPosition(Document document);
}
