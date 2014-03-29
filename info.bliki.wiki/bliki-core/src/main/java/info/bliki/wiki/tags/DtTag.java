package info.bliki.wiki.tags;

public class DtTag extends HTMLBlockTag {
	public DtTag() {
		super("dt", "|dl|");
	}

	@Override
	public Object clone() {
		DtTag dtt = new DtTag();
		return dtt;
	}
}