-- This file is simplification of the situation that "Module:ru-verb" calls "Module:TemplateStyles" with named arguments in enwiktionary.
local export = {}
function export.show(frame)
    return require("Module:parser-function-caller")("named-args", { abc = 987, [123] = "zyx" })
end
return export
