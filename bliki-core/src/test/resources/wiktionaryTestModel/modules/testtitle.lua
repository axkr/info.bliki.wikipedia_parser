local export = {}

function export.showtitle(frame)
	return "Current Title = " .. mw.title.getCurrentTitle().text
end

return export
