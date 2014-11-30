local export = {}

local lemmas = {
    ["abbreviations"] = true,
    ["acronyms"] = true,
    ["adjectives"] = true,
    ["adnominals"] = true,
    ["adpositions"] = true,
    ["adverbs"] = true,
    ["affixes"] = true,
    ["articles"] = true,
    ["circumfixes"] = true,
    ["circumpositions"] = true,
    ["classifiers"] = true,
    ["cmavo"] = true,
    ["cmavo clusters"] = true,
    ["cmene"] = true,
    ["comparative adjectives"] = true,
    ["comparative adverbs"] = true,
    ["conjunctions"] = true,
    ["contractions"] = true,
    ["counters"] = true,
    ["determiners"] = true,
    ["diacritical marks"] = true,
    ["fu'ivla"] = true,
    ["gismu"] = true,
    ["Han characters"] = true,
    ["Han tu"] = true,
    ["hanzi"] = true,
    ["hanja"] = true,
    ["idioms"] = true,
    ["infixes"] = true,
    ["interfixes"] = true,
    ["initialisms"] = true,
    ["interjections"] = true,
    ["kanji"] = true,
    ["letters"] = true,
    ["ligatures"] = true,
    ["lujvo"] = true,
    ["morphemes"] = true,
    ["non-constituents"] = true,
    ["nouns"] = true,
    ["numbers"] = true,
    ["numeral symbols"] = true,
    ["numerals"] = true,
    ["particles"] = true,
    ["phrases"] = true,
    ["postpositions"] = true,
    ["predicatives"] = true,
    ["prefixes"] = true,
    ["prepositions"] = true,
    ["prepositional phrases"] = true,
    ["preverbs"] = true,
    ["pronominal adverbs"] = true,
    ["pronouns"] = true,
    ["proverbs"] = true,
    ["proper nouns"] = true,
    ["punctuation marks"] = true,
    ["relatives"] = true,
    ["roots"] = true,
    ["suffixes"] = true,
    ["superlative adjectives"] = true,
    ["superlative adverbs"] = true,
    ["syllables"] = true,
    ["symbols"] = true,
    ["verbs"] = true,
}

local nonlemmas = {
    ["active participles"] = true,
    ["adjectival participles"] = true,
    ["adjective forms"] = true,
    ["adjective comparative forms"] = true,
    ["adjective equative forms"] = true,
    ["adjective superlative forms"] = true,
    ["adverb forms"] = true,
    ["adverb comparative forms"] = true,
    ["adverb superlative forms"] = true,
    ["adverbial participles"] = true,
    ["agent participles"] = true,
    ["article forms"] = true,
    ["circumfix forms"] = true,
    ["combined forms"] = true,
    ["determiner comparative forms"] = true,
    ["determiner forms"] = true,
    ["determiner superlative forms"] = true,
    ["diminutive nouns"] = true,
    ["gerunds"] = true,
    ["infinitives"] = true,
    ["jyutping"] = true,
    ["kanji readings"] = true,
    ["misspellings"] = true,
    ["negative participles"] = true,
    ["nominal participles"] = true,
    ["noun case forms"] = true,
    ["noun forms"] = true,
    ["noun plural forms"] = true,
    ["numeral forms"] = true,
    ["participles"] = true,
    ["participle forms"] = true,
    ["particle forms"] = true,
    ["passive participles"] = true,
    ["past active participles"] = true,
    ["past participles"] = true,
    ["past participle forms"] = true,
    ["past passive participles"] = true,
    ["perfect active participles"] = true,
    ["perfect passive participles"] = true,
    ["pinyin"] = true,
    ["plurals"] = true,
    ["postposition forms"] = true,
    ["prefix forms"] = true,
    ["preposition contractions"] = true,
    ["preposition forms"] = true,
    ["prepositional pronouns"] = true,
    ["present active participles"] = true,
    ["present participles"] = true,
    ["present passive participles"] = true,
    ["pronoun forms"] = true,
    ["proper noun forms"] = true,
    ["rafsi"] = true,
    ["romanizations"] = true,
    ["singulatives"] = true,
    ["suffix forms"] = true,
    ["verb forms"] = true,
    ["verbal nouns"] = true,
}

local notranslit = {
    ["az"] = true,
    ["bbc"] = true,
    ["bug"] = true,
    ["cia"] = true,
    ["cjm"] = true,
    ["cmn"] = true,
    ["hak"] = true,
    ["ja"] = true,
    ["lad"] = true,
    ["lzh"] = true,
    ["ms"] = true,
    ["mul"] = true,
    ["nan"] = true,
    ["oj"] = true,
    ["pi"] = true,
    ["ro"] = true,
    ["ryu"] = true,
    ["sh"] = true,
    ["tgt"] = true,
    ["tly"] = true,
    ["und"] = true,
    ["vi"] = true,
    ["yue"] = true,
    ["zh"] = true,
}


local function preprocess(lang, sc, heads, translits, categories)
    if type(heads) ~= "table" then
        heads = {heads}
    end

    if type(translits) ~= "table" then
        translits = {translits}
    end

    if #heads == 0 then
        heads = {""}
    end

    -- Create a default headword.
    local default_head = mw.title.getCurrentTitle().subpageText

    -- Determine if term is reconstructed
    local is_reconstructed = lang:getType() == "reconstructed" or (lang:getType() ~= "appendix-constructed" and mw.title.getCurrentTitle().nsText == "Appendix")

    -- Add links to multi-word page names when appropriate
    if lang:getCode() ~= "zh" then
        local WORDBREAKCHARS = "([%p%s]+)"
        local EXCLUDECHARS = "([^-־'.·]+)" -- workaround for excluding characters from the above
        local contains_words = false; mw.ustring.gsub(default_head, WORDBREAKCHARS, function(b) contains_words = contains_words or mw.ustring.match(b, "^" .. EXCLUDECHARS .. "$"); end)

        if (not is_reconstructed) and contains_words then
            local function workaround_to_exclude_chars(s)
                return mw.ustring.gsub(s, EXCLUDECHARS, "]]%1[[")
            end

            default_head = "[[" .. mw.ustring.gsub(default_head, WORDBREAKCHARS, workaround_to_exclude_chars) .. "]]"
            -- default_head = "[[" .. mw.ustring.gsub(default_head, WORDBREAKCHARS, "]]%1[[") .. "]]" -- use this when workaround is no longer needed
            default_head = mw.ustring.gsub(default_head, "%[%[%]%]", "") -- remove any empty links (which could have been created above at the beginning or end of the string)
        end
    end

    if is_reconstructed then
        default_head = "*" .. default_head
    end

    -- If a head is the empty string "", then replace it with the default
    for i, head in ipairs(heads) do
        if head == "" then
            head = default_head
        end

        heads[i] = head
    end

    -- Try to detect the script if it was not provided
    -- We use the first headword for this, and assume that all of them have the same script
    -- This *should* always be true, right?
    if not sc then
        sc = require("Module:scripts").findBestScript(heads[1], lang)
    end

    -- Make transliterations
    for i, head in ipairs(heads) do
        local translit = translits[i]

        -- Try to generate a transliteration if necessary
        -- Generate it if the script is not Latn or similar, and if no transliteration was provided
        if translit == "-" then
            translit = nil
        elseif not translit and not ((sc:getCode():find("Latn", nil, true)) or sc:getCode() == "Latinx" or sc:getCode() == "None") then
            translit = lang:transliterate(require("Module:links").remove_links(head), sc)

            -- There is still no transliteration?
            -- Add the entry to a cleanup category.
            if not translit and not notranslit[lang:getCode()] then
                translit = "<small>transliteration needed</small>"
                table.insert(categories, lang:getCanonicalName() .. " terms needing transliteration")
            end
        end

        translits[i] = translit
    end

    return heads, translits, sc
end


-- Format a headword with transliterations
local function format_headword(lang, sc, heads, translits)
    local m_links = require("Module:links")
    local m_scriptutils = require("Module:script utilities")

    -- Are there non-empty transliterations?
    -- Need to do it this way because translit[1] might be nil while translit[2] is not
    local has_translits = false

    -- Format the headwords
    for i, head in ipairs(heads) do
        if translits[i] then
            has_translits = true
        end

        -- Apply processing to the headword, for formatting links and such
        if head:find("[[", nil, true) then
            head = m_links.language_link(head, nil, lang, nil, false)
        end

        -- Add language and script wrapper
        head = m_scriptutils.tag_text(head, lang, sc, "head")

        heads[i] = head
    end

    local translits_formatted = ""

    if has_translits then
        -- Format the transliterations
        for i, head in ipairs(heads) do
            local translit = translits[i]

            if not translit then
                translit = "?"
            end

            translit = "<span class=\"tr\" lang=\"\">" .. translit .. "</span>"

            translits[i] = translit
        end

        translits_formatted = " (<span class=\"tr\" lang=\"\">" .. table.concat(translits, " ''or'' ") .. "</span>)"

        if lang and mw.title.new(lang:getCanonicalName() .. " transliteration", "Wiktionary").exists then
            translits_formatted = " [[Wiktionary:" .. lang:getCanonicalName() .. " transliteration|•]]" .. translits_formatted
        end
    end

    return table.concat(heads, " ''or'' ") .. translits_formatted
end


local function format_genders(lang, sc, genders)
    if genders and #genders > 0 then
        local gen = require("Module:gender and number")
        return "&nbsp;" .. gen.format_list(genders, lang)
    else
        return ""
    end
end


local function format_inflection_parts(lang, sc, parts)
    local m_links = require("Module:links")

    for key, part in ipairs(parts) do
        if type(part) ~= "table" then
            part = {term = part}
        end

        local qualifiers = ""

        if part.qualifiers and #part.qualifiers > 0 then
            qualifiers = mw.getCurrentFrame():expandTemplate{title = "qualifier", args = part.qualifiers} .. " "
        end

        -- Convert the term into a full link
        -- Don't show a transliteration here, the consensus seems to be not to
        -- show them in headword lines to avoid clutter.
        part = m_links.full_link(not parts.nolink and part.term or nil, part.alt or (parts.nolink and part.term or nil), lang, part.sc or sc, "bold", part.id, {genders = part.genders, tr = part.translit or (lang:getCode() ~= "ar" and "-" or nil)}, false)

        if parts.accel then
            part = "<span class=\"form-of lang-" .. lang:getCode() .. " " .. parts.accel .. "\">" .. part .. "</span>"
        end

        part = qualifiers .. part

        parts[key] = part
    end

    return "''" .. parts.label .. "''" .. (#parts > 0 and " " .. table.concat(parts, " ''or'' ") or "")
end

-- Format the inflections following the headword
local function format_inflections(lang, sc, inflections)
    if inflections and #inflections > 0 then
        -- Format each inflection individually
        for key, infl in ipairs(inflections) do
            -- If this inflection is a table, it contains parts
            -- consisting of alternating label-term pairs. Format them too.
            if type(infl) == "table" then
                inflections[key] = format_inflection_parts(lang, sc, infl)
            end
        end

        return " (" .. table.concat(inflections, ", ") .. ")"
    else
        return ""
    end
end

function export.full_headword(lang, sc, heads, translits, genders, inflections, categories, sort_key)
    local tracking_categories = {}

    categories = categories or {}

    -- Were any categories specified?
    if #categories == 0 then
        if lang:getCode() ~= "und" then
            table.insert(tracking_categories, "head tracking/no pos")
            require("Module:debug").track("head tracking/no pos")
            require("Module:debug").track("head tracking/no pos/lang/" .. lang:getCode())
        end
    else
        for _, cat in ipairs(categories) do
            -- Does the category begin with the language name? If not, tag it with a tracking category.
            if mw.ustring.sub(cat, 1, mw.ustring.len(lang:getCanonicalName()) + 1) ~= lang:getCanonicalName() .. " " then
                table.insert(tracking_categories, "head tracking/no lang category")
                require("Module:debug").track("head tracking/no lang category")
                require("Module:debug").track("head tracking/no lang category/lang/" .. lang:getCode())
            end
        end

        -- Does the category begin with the language name?
        if mw.ustring.sub(categories[1], 1, mw.ustring.len(lang:getCanonicalName()) + 1) == lang:getCanonicalName() .. " " then
            local pos = mw.ustring.sub(categories[1], mw.ustring.len(lang:getCanonicalName()) + 2)

            -- Is it a lemma category?
            if lemmas[pos] or lemmas[pos:gsub("^reconstructed ", "")] then
                table.insert(categories, 1, lang:getCanonicalName() .. " lemmas")
                -- Is it a nonlemma category?
            elseif nonlemmas[pos] or nonlemmas[pos:gsub("^reconstructed ", "")] or lemmas[pos:gsub("^mutated ", "")] or nonlemmas[pos:gsub("^mutated ", "")] then
                table.insert(categories, lang:getCanonicalName() .. " non-lemma forms")
                -- It's neither; we don't know what this category is, so tag it with a tracking category.
            else
                table.insert(tracking_categories, "head tracking/unrecognized pos")
                require("Module:debug").track("head tracking/unrecognized pos")
                require("Module:debug").track("head tracking/unrecognized pos/lang/" .. lang:getCode())
                require("Module:debug").track("head tracking/unrecognized pos/pos/" .. pos)
            end
        end
    end

    -- Preprocess
    heads, translits, sc = preprocess(lang, sc, heads, translits, categories)

    -- Format and return all the gathered information
    return
    format_headword(lang, sc, heads, translits) ..
            format_genders(lang, sc, genders) ..
            format_inflections(lang, sc, inflections) ..
            require("Module:utilities").format_categories(categories, lang, sort_key) ..
            require("Module:utilities").format_categories(tracking_categories, lang, sort_key)
end

return export
