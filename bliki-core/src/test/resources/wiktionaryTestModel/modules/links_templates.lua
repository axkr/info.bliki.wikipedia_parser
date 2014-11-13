local m_links = require("Module:links")

local export = {}

-- Used in [[Template:l]] and [[Template:m]]
function export.l_term_t(frame, fakeargs)
    local face = frame.args["face"]
    local allowSelfLink = frame.args["notself"]; allowSelfLink = not allowSelfLink or allowSelfLink == ""
    -- Compatibility mode.
    -- If given a nonempty value, the function uses lang= to specify the
    -- language, and all the positional parameters shift one number lower.
    local compat = (frame.args["compat"] or "") ~= ""

    local args = fakeargs or frame:getParent().args

    local lang = args[(compat and "lang" or 1)]; if lang == "" then lang = nil end
    local sc = args["sc"]; if sc == "" then sc = nil end

    local term = args[(compat and 1 or 2)]; if term == "" then term = nil end
    local alt = args[(compat and 2 or 3)]; if alt == "" then alt = nil end
    local id = args["id"]; if id == "" then id = nil end

    local tr = args["tr"]; if tr == "" then tr = nil end
    local gloss = args["gloss"] or args[(compat and 3 or 4)]; if gloss == "" then gloss = nil end
    local pos = args["pos"]; if pos == "" then pos = nil end
    local lit = args["lit"]; if lit == "" then lit = nil end

    -- Gather gender and number specifications
    -- Iterate over all gn parameters (g2, g3 and so on) until one is empty
    local genders = {}
    local g = args["g"] or ""
    local i = 2

    while g ~= "" do
        table.insert(genders, g)
        g = args["g" .. i] or ""
        i = i + 1
    end

    -- Check parameters
    if not lang then
        -- Temporary. Unfortunately, many pages are missing the language parameter.
        -- These all need to be fixed, but until then this is needed to avoid
        -- thousands of script errors. See [[:Category:term cleanup]]!
        if compat or mw.title.getCurrentTitle().nsText == "Template" then
            lang = "und"
        else
            error("The first parameter (language code) has not been given")
        end
    end

    lang = require("Module:languages").getByCode(lang) or error("The language code \"" .. lang .. "\" is not valid.")
    sc = (sc and (require("Module:scripts").getByCode(sc) or error("The script code \"" .. sc .. "\" is not valid.")) or nil)

    if not term and not alt and mw.title.getCurrentTitle().nsText == "Template" then
        term = "term"
    end

    -- Forward the information to full_link
    return m_links.full_link(term, alt, lang, sc, face, id, {tr = tr, genders = genders, gloss = gloss, pos = pos, lit = lit}, allowSelfLink)
end

function export.ll(frame)
    local args = frame:getParent().args

    local text = args[2]
    local alt = args[3]
    if text == "" then return alt or "" end
    if alt == "" then alt = nil end

    local lang = args[1]
    lang = require("Module:languages").getByCode(lang) or error("The language code \"" .. lang .. "\" is not valid.")

    local id = args["id"]; if id == "" then id = nil end
    local allowSelfLink = args["allowSelfLink"]; allowSelfLink = not not (allowSelfLink and allowSelfLink ~= "")

    return m_links.language_link(text, alt, lang, id, allowSelfLink)
end

return export
