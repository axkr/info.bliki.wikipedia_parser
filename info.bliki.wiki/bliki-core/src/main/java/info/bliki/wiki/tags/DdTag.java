package info.bliki.wiki.tags;

public class DdTag extends HTMLBlockTag {
	public DdTag() {
		super("dd", "|dl|");
	}

	@Override
	public Object clone() {
		DdTag ddt = new DdTag();
		return ddt;
	}
}