local export = {}

local invariable = {
    ["cmavo"] = true,
    ["cmene"] = true,
    ["fu'ivla"] = true,
    ["gismu"] = true,
    ["Han tu"] = true,
    ["hanzi"] = true,
    ["hanja"] = true,
    ["jyutping"] = true,
    ["kanji"] = true,
    ["lujvo"] = true,
    ["phrasebook"] = true,
    ["pinyin"] = true,
    ["rafsi"] = true,
    ["romaji"] = true,
}

function export.head_t(frame)
    local args = frame:getParent().args

    -- Get language and script information
    local lang = args[1] or (mw.title.getCurrentTitle().nsText == "Template" and "und") or error("Language code has not been specified. Please pass parameter 1 to the template.")
    local sc = args["sc"] or ""; if sc == "" then sc = nil end
    local cat_sc = args["cat sc"] or ""; if cat_sc == "" then cat_sc = nil end
    lang = require("Module:languages").getByCode(lang) or error("The language code \"" .. lang .. "\" is not valid.")

    if cat_sc then
        cat_sc = (cat_sc and (require("Module:scripts").getByCode(cat_sc) or error("The script code \"" .. cat_sc .. "\" is not valid.")) or nil)
        sc = cat_sc
    else
        sc = (sc and (require("Module:scripts").getByCode(sc) or error("The script code \"" .. sc .. "\" is not valid.")) or nil)
    end

    -- Gather basic parameters
    local sort_key = args["sort"]; if sort_key == "" then sort_key = nil end
    local pos = args[2]; if pos == "" then pos = nil end
    local cat2 = args["cat2"]; if cat2 == "" then cat2 = nil end
    local cat3 = args["cat3"]; if cat3 == "" then cat3 = nil end

    -- Gather headwords and transliterations
    local heads = {}
    local translits = {}
    local head = args["head"] or ""
    local translit = args["tr"]; if translit == "" then translit = nil end
    local i = 1

    while head do
        if head then
            table.insert(heads, head)
            translits[#heads] = translit
        end

        i = i + 1
        head = args["head" .. i]; if head == "" then head = nil end
        translit = args["tr" .. i]; if translit == "" then translit = nil end
    end

    -- Gather gender and number specifications
    -- Iterate over all gn parameters (g2, g3 and so on) until one is empty
    local genders = {}
    local g = args["g"]; if g == "" then g = nil end
    local i = 2

    while g do
        table.insert(genders, g)
        g = args["g" .. i]; if g == "" then g = nil end
        i = i + 1
    end

    -- Gather inflected forms
    local inflections = {}

    local i = 1
    local label = args[i * 2 + 1]
    local accel = args["f" .. i .. "accel"]; if accel == "" then accel = nil end
    local nolink = args["f" .. i .. "nolink"]; if nolink == "" then nolink = nil end
    local parts = {label = label, accel = accel, nolink = nolink}

    local m_scripts = nil

    while label do
        local term = args[i * 2 + 2]; if term == "" then term = nil end
        local alt = args["f" .. i .. "alt"]; if alt == "" then alt = nil end
        local sc = args["f" .. i .. "sc"]; if sc == "" then sc = nil end
        local id = args["f" .. i .. "id"]; if id == "" then id = nil end
        local translit = args["f" .. i .. "tr"]; if translit == "" then translit = nil end
        local gender = args["f" .. i .. "g"]; if gender == "" then gender = nil end
        local qualifier = args["f" .. i .. "qual"]; if qualifier == "" then qualifier = nil end

        if term or alt then
            if sc then
                m_scripts = m_scripts or require("Module:scripts")
                sc = (sc and (m_scripts.getByCode(sc) or error("The script code \"" .. sc .. "\" is not valid.")) or nil)
            end

            table.insert(parts, {term = term, alt = alt, sc = sc, id = id, translit = translit, genders = {gender}, qualifiers = {qualifier}})
        end

        i = i + 1
        label = args[i * 2 + 1]
        accel = args["f" .. i .. "accel"]; if accel == "" then accel = nil end
        nolink = args["f" .. i .. "nolink"]; if nolink == "" then nolink = nil end

        -- If the next label is not "or" then insert the previous one and create a new one.
        if label ~= "or" then
            -- Only insert if the previous label is not empty.
            if (parts.label or "") ~= "" then
                table.insert(inflections, parts)
            end

            parts = {label = label, accel = accel, nolink = nolink}
        end
    end

    -- Get/set categories
    local categories = {}
    local tracking_categories = {}

    if pos then
        if not pos:find("s$") and not invariable[pos] then
            --require("Module:debug").track("head tracking/singular category")

            -- Make the plural form of the part of speech
            if pos:find("x$") then
                pos = pos .. "es"
            else
                pos = pos .. "s"
            end
        end

        table.insert(categories, lang:getCanonicalName() .. " " .. pos .. (cat_sc and " in " .. cat_sc:getCategoryName() or ""))
    end

    if cat2 then
        table.insert(categories, lang:getCanonicalName() .. " " .. cat2)
    end

    if cat3 then
        table.insert(categories, lang:getCanonicalName() .. " " .. cat3)
    end

    return require("Module:headword").full_headword(lang, sc, heads, translits, genders, inflections, categories, sort_key) ..
            require("Module:utilities").format_categories(tracking_categories, lang, sort_key)
end

return export
