local m_ja = require("Module:ja")
local export = {}

function export.show(frame)
    local args = frame:getParent().args
    local lemma = args[1] or error("Page to be linked to has not been specified. Please pass parameter 1 to the module invocation.")
    local kana = args[2] or ""
    -- if only one arg passed, it may be kana with hyphens
    if kana == "" then
        kana = lemma
        lemma = mw.ustring.gsub(lemma, '[%-]', '')
    end
    local gloss = args["gloss"] or ""
    local linkto = args["linkto"] or ""
    local caps = args["caps"] or ""
    local transliteration = m_ja.kana_to_romaji(kana)
    if caps ~= "" then
        transliteration = mw.ustring.gsub(transliteration, "^%l", mw.ustring.upper)
        transliteration = mw.ustring.gsub(transliteration, " %l", mw.ustring.upper)
    end
    transliteration = "''" .. transliteration .. "''"

    if gloss == "" and linkto == "" then
        return frame:expandTemplate{ title = "l", args = {"ja", lemma, m_ja.add_ruby_backend(lemma, kana), sc = "Jpan", tr = transliteration}}
    elseif linkto == "" then
        return frame:expandTemplate{ title = "l", args = {"ja", lemma, m_ja.add_ruby_backend(lemma, kana), gloss = gloss, sc = "Jpan", tr = transliteration}}
    else
        return frame:expandTemplate{ title = "l", args = {"ja", linkto, m_ja.add_ruby_backend(lemma, kana), gloss = gloss, sc = "Jpan", tr = transliteration}}
    end
end

return export
