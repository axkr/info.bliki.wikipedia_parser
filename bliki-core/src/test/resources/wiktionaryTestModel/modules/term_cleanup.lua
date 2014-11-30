local export = {}

function export.cleanup(frame)
    local args = frame:getParent().args
    local title = mw.title.getCurrentTitle()

    local lang = args["lang"]
    local sc = args["sc"]
    local tr = args["tr"]
    local term = (args[1] or "") .. (args[2] or "")

    if lang == "und" then
        return "[[Category:term cleanup/lang=und]]"
    elseif not lang or lang == "" then
        if title.nsText == "Template" then
            return "[[Category:term cleanup/template namespace]]"
        elseif sc then
            if sc == "Arab" then
                return "[[Category:term cleanup/sc=Arab]]"
            elseif sc == "fa-Arab" then
                return "[[Category:term cleanup/sc=fa-Arab]]"
            elseif sc == "Deva" then
                return "[[Category:term cleanup/sc=Deva]]"
            elseif sc == "Grek" then
                return "[[Category:term cleanup/sc=Grek]]"
            elseif sc == "Hebr" then
                return "[[Category:term cleanup/sc=Hebr]]"
            elseif sc == "Hani" or sc == "Hant" or sc == "Hans" then
                return "[[Category:term cleanup/sc=Hani]]"
            else
                return "[[Category:term cleanup/with sc]]"
            end
        elseif lang == "" then
            return "[[Category:term cleanup/lang=]]"
        elseif title.isTalkPage then
            return "[[Category:term cleanup/talk]]"
        elseif title.nsText == "User" then
            return "[[Category:term cleanup/user namespace]]"
        elseif title.nsText == "Wiktionary" then
            return "[[Category:term cleanup/wiktionary namespace]]"
        elseif tr then
            return "[[Category:term cleanup/with tr]]"
            -- Categorise by character range
        elseif containsRange(term, 0x0600, 0x06FF) then
            return "[[Category:term cleanup/Arabic]]"
        elseif containsRange(term, 0x0530, 0x058F) then
            return "[[Category:term cleanup/Armenian]]"
        elseif containsRange(term, 0x3400, 0x9FFF) then
            return "[[Category:term cleanup/Chinese]]"
        elseif containsRange(term, 0x0400, 0x04FF) then
            return "[[Category:term cleanup/Cyrillic]]"
        elseif containsRange(term, 0x0900, 0x097F) then
            return "[[Category:term cleanup/Devanagari]]"
        elseif containsRange(term, 0x10A0, 0x10FF) then
            return "[[Category:term cleanup/Georgian]]"
        elseif containsRange(term, 0x2C00, 0x2C5F) then
            return "[[Category:term cleanup/Glagolitic]]"
        elseif containsRange(term, 0x10330, 0x1034F) then
            return "[[Category:term cleanup/Gothic]]"
        elseif containsRange(term, 0x0370, 0x03FF) then
            return "[[Category:term cleanup/Greek]]"
        elseif containsRange(term, 0x1F00, 0x1FFF) then
            return "[[Category:term cleanup/Polytonic Greek]]"
        elseif containsRange(term, 0x0590, 0x05FF) or containsRange(term, 0xFB1D, 0xFB4F) then
            return "[[Category:term cleanup/Hebrew]]"
        elseif containsRange(term, 0x3040, 0x30FF) then
            return "[[Category:term cleanup/Kana]]"
        elseif containsRange(term, 0xAC00, 0xD7AF) then
            return "[[Category:term cleanup/Korean]]"
        elseif containsRange(term, 0x0E00, 0x0E7F) then
            return "[[Category:term cleanup/Thai]]"
        elseif containsRange(term, 0x80, 0x024F) then
            return "[[Category:term cleanup/Latin extended]]"
            -- Any other non-ASCII
        elseif containsRange(term, 0x80, 0x10FFFF) then
            return "[[Category:term cleanup/containing characters]]"
        elseif term:find("@") then
            return "[[Category:term cleanup/sign language]]"
        else
            return "[[Category:term cleanup]]"
        end
    end
end

function containsRange(str, first, last)
    for codepoint in mw.ustring.gcodepoint(str) do
        if codepoint >= first and codepoint <= last then
            return true
        end
    end
    return false
end

return export
