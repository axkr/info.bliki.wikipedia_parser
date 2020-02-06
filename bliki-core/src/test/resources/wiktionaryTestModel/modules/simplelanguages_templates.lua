local export = {}

-- Used by the following JS:
-- * [[WT:ACCEL]]
-- * [[WT:EDIT]]
-- * [[WT:NEC]]
function export.getByCode(frame)
	local iparams = {
		[1] = {required = true},
		[2] = {required = true},
		[3] = {},
		[4] = {},
		[5] = {},
	}

	local iargs = require("Module:parameters").process(frame.args, iparams)
	local langcode = iargs[1]
  print(langcode)

	if langCode == "en" then
	  return "English"
	else
	  return "PROBLEM"
	end
end

return export
