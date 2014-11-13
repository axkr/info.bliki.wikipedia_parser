local export = {}

--TODO: move to [[Module:languages]]
local override_translit = {
    ["ab"] = true,
    ["abq"] = true,
    ["ady"] = true,
    ["am"] = true,
    ["av"] = true,
    ["axm"] = true,
    ["ba"] = true,
    ["bo"] = true,
    ["bua"] = true,
    ["ce"] = true,
    ["chm"] = true,
    ["cv"] = true,
    ["dar"] = true,
    ["dv"] = true,
    ["dz"] = true,
    ["el"] = true,
    ["got"] = true,
    ["grc"] = true,
    ["hy"] = true,
    ["inh"] = true,
    ["iu"] = true,
    ["ka"] = true,
    ["kk"] = true,
    --["ko"] = true,
    ["kbd"] = true,
    ["kca"] = true,
    ["kjh"] = true,
    ["kn"] = true,
    ["koi"] = true,
    ["kpv"] = true,
    ["ky"] = true,
    ["kv"] = true,
    ["lo"] = true,
    ["lbe"] = true,
    ["lez"] = true,
    ["lzz"] = true,
    ["mdf"] = true,
    ["ml"] = true,
    ["mn"] = true,
    ["my"] = true,
    ["myv"] = true,
    ["oge"] = true,
    ["os"] = true,
    ["sah"] = true,
    ["si"] = true,
    ["sva"] = true,
    ["ta"] = true,
    ["tab"] = true,
    ["te"] = true,
    ["ti"] = true,
    ["tg"] = true,
    ["tt"] = true,
    ["tyv"] = true,
    ["ug"] = true,
    ["udm"] = true,
    ["xal"] = true,
    ["xcl"] = true,
    ["xmf"] = true,
}

local ignore_cap = {
    ["ko"] = true,
}


-- Make a language-specific link from given link's parts
local function makeLangLink(link, lang, id, allowSelfLink)
    -- If there is no display form, then create a default one
    if not link.display then
        link.display = link.target

        -- Strip the prefix from the displayed form
        -- TODO: other interwiki links?
        if link.display:find("^:") then
            link.display = link.display:gsub("^:", "")
        elseif link.display:find("^w:") then
            link.display = link.display:gsub("^w:", "")
        end
    end

    -- Process the target
    if not (link.target:find("^:") or link.target:find("^w:")) then
        -- Remove diacritics from the page name
        link.target = lang:makeEntryName(link.target)

        -- Link to appendix for reconstructed terms and terms in appendix-only languages
        if link.target:find("^*.") then
            if lang:getCode() == "und" then
                return link.display
            else
                link.target = "Appendix:" .. lang:getCanonicalName() .. "/" .. mw.ustring.sub(link.target, 2)
            end
        elseif lang:getType() == "reconstructed" then
            error("The specified language " .. lang:getCanonicalName() .. " is unattested, while the given word is not marked with '*' to indicate that it is reconstructed")
        elseif lang:getType() == "appendix-constructed" then
            link.target = "Appendix:" .. lang:getCanonicalName() .. "/" .. link.target
        end
    end

    -- If the target is the same as the current page, then return a "self-link" like the software does
    if not allowSelfLink and (link.target == mw.title.getCurrentTitle().prefixedText or link.target == ":" .. mw.title.getCurrentTitle().prefixedText) then
        return "<strong class=\"selflink\">" .. link.display .. "</strong>"
    end

    -- Add fragment
    -- Do not add a section link to "Undetermined", as such sections do not exist and are invalid.
    -- TabbedLanguages handles links without a section by linking to the "last visited" section,
    -- but adding "Undetermined" would break that feature.
    if not link.fragment then
        if lang:getCode() ~= "und" and not link.target:find("^w:") and not link.target:find("^Appendix:") then
            if id then
                link.fragment = lang:getCanonicalName() .. "-" .. id
            elseif not link.target:find("^Appendix:") then
                link.fragment = lang:getCanonicalName()
            end
        end
    end

    return "[[" .. link.target .. (link.fragment and "#" .. link.fragment or "") .. "|" .. link.display .. "]]"
end


-- Split a link into its parts
local function parseLink(linktext)
    local link = {target = linktext}
    local found, _, first, second

    found, _, first, second = mw.ustring.find(link.target, "^([^|]+)|(.+)$")

    if found then
        link.target = first
        link.display = second
    else
        link.display = link.target
    end

    found, _, first, second = mw.ustring.find(link.target, "^(.+)#(.+)$")

    if found then
        link.target = first
        link.fragment = second
    end

    return link
end


-- Creates a basic wikilink to the given term. If the text already contains
-- links, these are replaced with links to the correct section.
local function language_link2(text, alt, lang, id, allowSelfLink)
    local sectFix = false
    local tracking = ""

    if text and text:gsub("&#[Xx]?[0-9A-Fa-f]+;", ""):find("#", nil, true) then
        sectFix = true
    end

    if ignore_cap[lang:getCode()] and text then
        text = mw.ustring.gsub(text, "%^", "")
    end

    -- If the text begins with * and another character,
    -- then act as if each link begins with *
    local allReconstructed = false

    if text:find("^*.") then
        allReconstructed = true
    end

    -- Do we have embedded wikilinks?
    if text:find("[[", nil, true) then
        if id then
            require("Module:debug").track("language link/bad id")
        end

        local function repl(linktext)
            local link = parseLink(linktext)

            if allReconstructed then
                link.target = "*" .. link.target
            end

            return makeLangLink(link, lang, id, allowSelfLink)
        end

        text = mw.ustring.gsub(text, "%[%[([^%]]+)%]%]", repl)

        -- Remove the extra * at the beginning if it's immediately followed
        -- by a link whose display begins with * too
        if allReconstructed then
            text = mw.ustring.gsub(text, "^%*%[%[([^|%]]+)|%*", "[[%1|*")
        end
    else
        -- There is no embedded wikilink, make a link using the parameters.
        text = makeLangLink({target = text, display = alt}, lang, id, allowSelfLink)
    end

    return text .. (sectFix and "[[Category:Link with section]]" or "") .. tracking
end


-- Format the annotations (things following the linked term)
local function format_link_annotations(lang, annotations, face)
    local ret = ""

    -- Interwiki link
    if annotations["interwiki"] then
        ret = ret .. annotations["interwiki"]
    end

    -- Genders
    if annotations["genders"] and #annotations["genders"] > 0 then
        local gen = require("Module:gender and number")
        ret = ret .. "&nbsp;" .. gen.format_list(annotations["genders"], lang)
    end

    local glosses = {}

    -- Transliteration
    if annotations["tr"] then
        if face == "term" then
            table.insert(glosses, "<span lang=\"\" class=\"tr mention-tr\">" .. annotations["tr"] .. "</span>")
        else
            table.insert(glosses, "<span lang=\"\" class=\"tr\">" .. annotations["tr"] .. "</span>")
        end
    end

    -- Gloss/translation
    if annotations["gloss"] then
        table.insert(glosses, "<span class=\"mention-gloss-double-quote\">“</span><span class=\"mention-gloss\">" .. annotations["gloss"] .. "</span><span class=\"mention-gloss-double-quote\">”</span>")
    end

    -- Part of speech
    -- TODO: remove
    if annotations["pos"] then
        local pos_template = mw.title.makeTitle("Template", "pos " .. annotations["pos"])

        if pos_template and pos_template.exists then
            table.insert(glosses, mw.getCurrentFrame():expandTemplate{title = "pos " .. annotations["pos"]})
        else
            table.insert(glosses, annotations["pos"])
        end
    end

    -- Literal/sum-of-parts meaning
    if annotations["lit"] then
        table.insert(glosses, "literally <span class=\"mention-gloss-double-quote\">“</span><span class=\"mention-gloss\">" .. annotations["lit"] .. "</span><span class=\"mention-gloss-double-quote\">”</span>")
    end

    if #glosses > 0 then
        ret = ret .. " (" .. table.concat(glosses, ", ") .. ")"
    end

    return ret
end


-- A version of {{l}} or {{m}} that can be called from other modules too
function export.full_link(term, alt, lang, sc, face, id, annotations, allowSelfLink)
    if allowSelfLink == false then
        require("Module:debug").track("curtitle/false")
    elseif allowSelfLink == true then
        require("Module:debug").track("curtitle/true")
    elseif allowSelfLink == nil then
        allowSelfLink = true
        require("Module:debug").track("curtitle/nil")
    else
        if allowSelfLink ~= mw.title.getCurrentTitle().prefixedText then
            require("Module:debug").track("curtitle/string/not pagename")
        end

        allowSelfLink = false
        require("Module:debug").track("curtitle/string")
    end

    annotations = annotations or {}

    -- Create the link
    local link = ""

    local m_utilities = require("Module:utilities")
    local m_scriptutils = require("Module:script utilities")

    -- Is there any text to show?
    if (term or alt) then
        -- Try to detect the script if it was not provided
        if not sc then
            sc = require("Module:scripts").findBestScript(alt or term, lang)
        end

        -- Only make a link if the term has been given, otherwise just show the alt text without a link
        link = m_scriptutils.tag_text(term and language_link2(term, alt, lang, id, allowSelfLink) or alt, lang, sc, face)
    else
        -- No term to show.
        -- Is there at least a transliteration we can work from?
        link = m_scriptutils.request_script(lang, sc)

        if link == "" or not annotations["tr"] or annotations["tr"] == "-" then
            -- No link to show, and no transliteration either. Show a term request.
            local category = ""
            if mw.title.getCurrentTitle().nsText ~= "Template" then
                category = "[[Category:" .. lang:getCanonicalName() .. " terms requested]]"
            end
            link = "<small>[Term?]</small>" .. category
        end
    end

    local mantrFix, redtrFix
    local manual_tr = ""

    if annotations["tr"] == "" or annotations["tr"] == "-" then
        annotations["tr"] = nil
    elseif (term or alt) and not ((sc:getCode():find("Latn", nil, true)) or sc:getCode() == "Latinx") and (not annotations["tr"] or override_translit[lang:getCode()]) then
        -- Try to generate a transliteration if necessary
        local automated_tr
        automated_tr = lang:transliterate(export.remove_links(alt or term), sc)
        if automated_tr then
            if annotations["tr"] ~= automated_tr then
                if annotations["tr"] then
                    manual_tr = annotations["tr"]
                    mantrFix = true
                end
                annotations["tr"] = automated_tr
            else
                redtrFix = true
            end
        end
    end

    return link .. format_link_annotations(lang, annotations, face)
            .. (mantrFix and "[[Category:Terms with manual transliterations different from the automated ones]][[Category:Terms with manual transliterations different from the automated ones/" .. lang:getCode() .. "]]" or "")
            .. (redtrFix and "[[Category:Terms with redundant transliterations]][[Category:Terms with redundant transliterations/" .. lang:getCode() .. "]]" or "")
end


function export.language_link(text, alt, lang, id, allowSelfLink)
    if allowSelfLink == false then
        require("Module:debug").track("curtitle/false")
    elseif allowSelfLink == true then
        require("Module:debug").track("curtitle/true")
    elseif allowSelfLink == nil then
        allowSelfLink = true
        require("Module:debug").track("curtitle/nil")
    else
        if allowSelfLink ~= mw.title.getCurrentTitle().prefixedText then
            require("Module:debug").track("curtitle/string/not pagename")
        end

        allowSelfLink = false
        require("Module:debug").track("curtitle/string")
    end

    require("Module:debug").track("language_link")
    return language_link2(text, alt, lang, id, allowSelfLink)
end


-- Strips all square brackets out or replaces them.
function export.remove_links(text)
    if type(text) == "table" then text = text.args[1] end; if not text then text = "" end

    text = text:gsub("%[%[[^|%]]-|", "")
    text = text:gsub("%[%[", "")
    text = text:gsub("%]%]", "")

    return text
end

return export
