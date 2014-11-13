-- When adding new scripts to this file, please don't forget to add
-- style definitons for the script in [[MediaWiki:Common.css]].

local u = mw.ustring.char
local m = {}

m["Afak"] = {
    names = { "Afaka" },
}

m["Aghb"] = {
    names = { "Caucasian Albanian" },
    characters = "𐔰-𐕣𐕯",
}

m["Arab"] = {
    names = { "Arabic", "Jawi" },
    characters = "؀-ۿݐ-ݿﭐ-﷽ﹰ-ﻼ",
}

m["fa-Arab"] = {
    names = { "Arabic", "Perso-Arabic" },
    characters = m["Arab"].characters,
}

m["kk-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["ks-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["ku-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["mzn-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["ota-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["pa-Arab"] = {
    names = { "Arabic", "Shahmukhi" },
    characters = m["Arab"].characters,
}

m["ps-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["sd-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["tt-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["ug-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["ur-Arab"] = {
    names = { "Arabic" },
    characters = m["Arab"].characters,
}

m["Armi"] = {
    names = { "Imperial Aramaic" },
    characters = "𐡀-𐡟",
}

m["Armn"] = {
    names = { "Armenian" },
    characters = "Ա-֏ﬓ-ﬗ",
}

m["Avst"] = {
    names = { "Avestan" },
    characters = "𐬀-𐬿",
}

m["Bali"] = {
    names = { "Balinese" },
    characters = "ᬀ-᭼",
}

m["Bamu"] = {
    names = { "Bamum" },
    characters = "ꚠ-꛷𖠀-𖨸",
}

m["Bass"] = {
    names = { "Bassa", "Bassa Vah", "Vah" },
    characters = "𖫐-𖫵",
}

m["Batk"] = {
    names = { "Batak" },
    characters = "ᯀ-᯿",
}

m["Beng"] = {
    names = { "Bengali" },
    characters = "ঁ-৺",
}

m["Bopo"] = {
    names = { "Zhuyin", "Zhuyin Fuhao", "Bopomofo" },
    characters = "ㄅ-ㄭㆠ-ㆺ",
}

m["Brah"] = {
    names = { "Brahmi" },
    characters = "𑀀-𑁯",
}

m["Brai"] = {
    names = { "Braille" },
    characters = "⠀-⣿",
}

m["Bugi"] = {
    names = { "Buginese" },
    characters = "ᨀ-᨟",
}

m["Buhd"] = {
    names = { "Buhid" },
    characters = "ᝀ-ᝓ",
}

m["Cakm"] = {
    names = { "Chakma" },
    characters = "𑄀-𑅃",
}

m["Cans"] = {
    names = { "Canadian syllabics" },
    characters = "᐀-ᙿ",
}

m["Cari"] = {
    names = { "Carian" },
    characters = "𐊠-𐋐",
}

m["Cham"] = {
    names = { "Cham" },
    characters = "ꨀ-꩟"
}

m["Cher"] = {
    names = { "Cherokee" },
    characters = "Ꭰ-Ᏼ",
}

m["Copt"] = {
    names = { "Coptic" },
    characters = "Ⲁ-⳿", -- This is the separate "Coptic" block, not the unified "Greek and Coptic"
}

m["Cprt"] = {
    names = { "Cypriot" },
    characters = "𐠀-𐠿",
}

m["Cyrl"] = {
    names = { "Cyrillic" },
    characters = "Ѐ-џҊ-ԧꚀ-ꚗ",
}

m["Cyrs"] = {
    names = { "Old Cyrillic" },
    characters = "Ѐ-ԧꙀ-ꚗ",
}

m["Deva"] = {
    names = { "Devanagari" },
    characters = "ऀ-ॿ꣠-ꣻ",
}

m["Dsrt"] = {
    names = { "Deseret" },
    characters = "𐐀-𐑏",
}

m["Dupl"] = {
    names = { "Duployan" },
    characters = "𛰀-𛲟",
}

m["Egyd"] = {
    names = { "Demotic" },
}

m["Egyp"] = {
    names = { "Egyptian hieroglyphic" },
    characters = "𓀀-𓐮",
}

m["Elba"] = {
    names = { "Elbasan" },
    characters = "𐔀-𐔧",
}

m["Ethi"] = {
    names = { "Ethiopic", "Ge'ez" },
    characters = "ሀ-᎙ⶀ-ⷞꬁ-ꬮ",
}

m["Geok"] = {
    names = { "Nuskhuri", "Khutsuri", "Asomtavruli" },
    characters = "Ⴀ-Ⴭⴀ-ⴭ", -- Ⴀ-Ⴭ is Asomtavruli, ⴀ-ⴭ is Nuskhuri
}

m["Geor"] = {
    names = { "Georgian", "Mkhedruli" },
    characters = "Ⴀ-ჼ", -- technically only the range [ა-ჿ] is Mkhedruli
}

m["Glag"] = {
    names = { "Glagolitic" },
    characters = "Ⰰ-ⱞ",
}

m["Goth"] = {
    names = { "Gothic" },
    characters = "𐌰-𐍊",
}

m["Gran"] = {
    names = { "Grantha" },
    characters = "𑌁-𑍴",
}

m["Grek"] = {
    names = { "Greek" },
    characters = "Ͱ-Ͽ",
}

m["polytonic"] = {
    names = { "Greek" },
    characters = "ἀ-῾" .. m["Grek"].characters,
}

m["Gujr"] = {
    names = { "Gujarati" },
    characters = "ઁ-૱",
}

m["Guru"] = {
    names = { "Gurmukhi" },
    characters = "ਁ-ੵ",
}

m["Hang"] = {
    names = { "Hangul" },
    characters = "가-힣",
}

m["Hani"] = {
    names = { "Han", "Hanzi", "Chu Nom" },
    characters = "一-鿌㐀-䶵　-〿𠀀-𫠝！-￮",
}

m["Hans"] = {
    names = { "Simplified Han" },
    characters = m["Hani"].characters,
}

m["Hant"] = {
    names = { "Traditional Han" },
    characters = m["Hani"].characters,
}

m["Hira"] = {
    names = { "Hiragana" },
    characters = "ぁ-ゟ",
}

m["Kana"] = {
    names = { "Katakana" },
    characters = "゠-ヿㇰ-ㇿ",
}

-- These should be defined after the scripts they are composed of

m["Jpan"] = {
    names = { "Japanese" },
    characters = m["Hira"].characters .. m["Kana"].characters .. m["Hani"].characters,
}

m["Kore"] = {
    names = { "Korean" },
    characters = m["Hang"].characters .. m["Hani"].characters .. "！-￮",
}

m["CGK"] = {
    names = { "Korean" },
}

m["Hano"] = {
    names = { "Hanunoo" },
    characters = "ᜠ-᜴",
}

m["Hebr"] = {
    names = { "Hebrew" },
    characters = u(0x0590) .. "-" .. u(0x05FF) .. u(0xFB1D) .. "-" .. u(0xFB4F),
}

m["Hmng"] = {
    names = { "Hmong", "Pahawh Hmong" },
    characters = "𖬀-𖮏",
}

m["Ibrn"] = {
    names = { "Iberian" },
}

m["Inds"] = {
    names = { "Indus", "Harappan", "Indus Valley" },
}

m["IPAchar"] = {
    names = { "International Phonetic Alphabet" },
}

m["Ital"] = {
    names = { "Old Italic" },
    characters = "𐌀-𐌣",
}

m["Java"] = {
    names = { "Javanese" },
    characters = "ꦀ-꧟",
}

m["Jurc"] = {
    names = { "Jurchen" },
}

m["Kali"] = {
    names = { "Kayah Li" },
    characters = "꤀-꤯",
}

m["Khar"] = {
    names = { "Kharoshthi" },
    characters = "𐨀-𐩘",
}

m["Khmr"] = {
    names = { "Khmer" },
    characters = "ក-៹",
}

m["Knda"] = {
    names = { "Kannada" },
    characters = "ಂ-ೲ",
}

m["Kthi"] = {
    names = { "Kaithi" },
    characters = "𑂀-𑃁",
}

m["Lana"] = {
    names = { "Lanna" },
}

m["Laoo"] = {
    names = { "Lao" },
    characters = "ກ-ໝ",
}

m["Latn"] = {
    names = { "Latin", "Roman", "Rumi", "Romaji", "Rōmaji" },
    characters = "0-9A-Za-z¡-ɏḀ-ỿ",
}

m["Latf"] = {
    names = { "Fraktur", "Blackletter" },
    characters = m["Latn"].characters,
}

m["Latinx"] = {
    names = { "Latin" },
    characters = m["Latn"].characters .. "Ⱡ-Ɀ꜠-ꟿꬰ-ꭥ",
}

m["nv-Latn"] = {
    names = { "Latin" },
    characters = m["Latn"].characters,
}

m["pjt-Latn"] = {
    names = { "Latin" },
    characters = m["Latn"].characters,
}

m["Lepc"] = {
    names = { "Lepcha" },
    characters = "ᰀ-ᱏ",
}

m["Limb"] = {
    names = { "Limbu" },
    characters = "ᤀ-᥏",
}

m["Lina"] = {
    names = { "Linear A" },
    characters = "𐘀-𐝧",
}

m["Linb"] = {
    names = { "Linear B" },
    characters = "𐀀-𐃺",
}

m["Lisu"] = {
    names = { "Lisu", "Fraser" },
    characters = "ꓐ-꓿",
}

m["Lyci"] = {
    names = { "Lycian" },
    characters = "𐊀-𐊜",
}

m["Lydi"] = {
    names = { "Lydian" },
    characters = "𐤠-𐤿",
}

m["Mand"] = {
    names = { "Mandaic" },
    characters = "ࡀ-࡞",
}

m["Mani"] = {
    names = { "Manichaean" },
    characters = "𐫀-𐫶",
}

m["Maya"] = {
    names = { "Maya", "Maya hieroglyphic", "Mayan", "Mayan hieroglyphic" },
}

m["Mend"] = {
    names = { "Mende", "Mende Kikakui" },
    characters = "𞠀-𞣖",
}

m["Merc"] = {
    names = { "Meroitic cursive" },
    characters = "𐦠-𐦿",
}

m["Mero"] = {
    names = { "Meroitic hieroglyphic" },
    characters = "𐦀-𐦟",
}

m["Mlym"] = {
    names = { "Malayalam" },
    characters = "ം-ൿ",
}

m["Mong"] = {
    names = { "Mongolian" },
    characters = "᠀-ᢪ",
}

m["Mtei"] = {
    names = { "Meitei Mayek" },
    characters = "ꯀ-꯿ꫠ-꫿",
}

m["musical"] = {
    names = { "musical notation" },
    characters = "𝄀-𝇝",
}

m["Mymr"] = {
    names = { "Burmese" },
    characters = "က-ၙ",
}

m["Nbat"] = {
    names = { "Nabataean", "Nabatean" },
    characters = "𐢀-𐢯",
}

m["Nkoo"] = {
    names = { "N'Ko" },
    characters = "߀-ߺ",
}

m["None"] = {
    names = { "No script specified" },
    -- This should not have any characters listed
    character_category = false, -- none
}

m["Ogam"] = {
    names = { "Ogham" },
    characters = " -᚜",
}

m["Olck"] = {
    names = { "Ol Chiki" },
    characters = "᱐-᱿",
}

m["Orkh"] = {
    names = { "Orkhon runes" },
    characters = "𐰀-𐱈",
}

m["Orya"] = {
    names = { "Oriya" },
    characters = "ଁ-୷",
}

m["Osma"] = {
    names = { "Osmanya" },
    characters = "𐒀-𐒩",
}

m["Palm"] = {
    names = { "Palmyrene" },
    characters = "-",
}

m["Phag"] = {
    names = { "Phags-pa" },
    characters = "ꡀ-꡷",
}

m["Phli"] = {
    names = { "Inscriptional Pahlavi" },
    characters = "𐭠-𐭿",
}

m["Phlp"] = {
    names = { "Psalter Pahlavi" },
    characters = "𐮀-𐮯",
}

m["Phlv"] = {
    names = { "Book Pahlavi" },
    -- Not in Unicode
}

m["Phnx"] = {
    names = { "Phoenician" },
    characters = "𐤀-𐤟",
}

m["Plrd"] = {
    names = { "Pollard" },
    characters = "𖼀-𖾟",
}

m["Prti"] = {
    names = { "Parthian" },
    characters = "𐭀-𐭟",
}

m["Rjng"] = {
    names = { "Rejang" },
    characters = "ꤰ-꥟",
}

m["Ruminumerals"] = {
    names = { "Rumi numerals" },
    characters = "𐹠-𐹾",
    character_category = "Rumi numerals",
}

m["Runr"] = {
    names = { "Runic" },
    characters = "ᚠ-ᛰ",
}

m["Samr"] = {
    names = { "Samaritan" },
    characters = "ࠀ-࠾",
}

m["Sarb"] = {
    names = { "Old South Arabian" },
    characters = "𐩠-𐩿",
}

m["Saur"] = {
    names = { "Saurashtra" },
    characters = "ꢀ-꣙",
}

m["Sgnw"] = {
    names = { "SignWriting" },
}

m["Shaw"] = {
    names = { "Shavian" },
    characters = "𐑐-𐑿",
}

m["Shrd"] = {
    names = { "Sharada" },
    characters = "𑆀-𑇙",
}

m["Sinh"] = {
    names = { "Sinhalese" },
    characters = "ං-෴",
}

m["Sora"] = {
    names = { "Sorang Sompeng", "Sora Sompeng" },
    characters = "𑃐-𑃹"
}

m["Sund"] = {
    names = { "Sundanese" },
    characters = "ᮀ-ᮿ",
}

m["Sylo"] = {
    names = { "Syloti Nagri", "Sylheti Nagari" },
}

m["Syrc"] = {
    names = { "Syriac" },
    characters = "܀-ݏ",
}

m["Tagb"] = {
    names = { "Tagbanwa" },
    characters = "ᝠ-ᝳ",
}

m["Takr"] = {
    names = { "Takri" },
    characters = "𑚀-𑛉",
}

m["Tale"] = {
    names = { "Tai Le" },
    characters = "ᥐ-ᥴ",
}

m["Talu"] = {
    names = { "New Tai Lue" },
    characters = "ᦀ-᧟",
}

m["Taml"] = {
    names = { "Tamil" },
    characters = "ஂ-௺",
}

m["Tang"] = {
    names = { "Tangut" },
}

m["Tavt"] = {
    names = { "Tai Viet" },
    characters = "ꪀ-꫟",
}

m["Telu"] = {
    names = { "Telugu" },
    characters = "ఁ-౿",
}

m["Teng"] = {
    names = { "Tengwar" },
}

m["Tfng"] = {
    names = { "Tifinagh" },
    characters = "ⴰ-⵿",
}

m["Tglg"] = {
    names = { "Tagalog" },
    characters = "ᜀ-᜔",
}

m["Thaa"] = {
    names = { "Thaana" },
    characters = "ހ-ޱ",
}

m["Thai"] = {
    names = { "Thai" },
    characters = "ก-ฺ",
}

m["Tibt"] = {
    names = { "Tibetan" },
    characters = "ༀ-࿚",
}

m["xzh-Tibt"] = {
    names = { "Zhang-Zhung" },
}

m["Ugar"] = {
    names = { "Ugaritic" },
    characters = "𐎀-𐎟",
}

m["Vaii"] = {
    names = { "Vai" },
    characters = "ꔀ-ꘫ",
}

m["Xpeo"] = {
    names = { "Old Persian" },
    characters = "𐎠-𐏕",
}

m["Xsux"] = {
    names = { "Cuneiform", "Sumero-Akkadian Cuneiform" },
    characters = "𒀀-𒍮𒐀-𒑳",
}

m["Yiii"] = {
    names = { "Yi" },
    characters = "ꀀ-꓆",
}

m["Zmth"] = {
    names = { "mathematical notation" },
    characters = "ℵ∀-⋿⟀-⟯⦀-⧿⨀-⫿𝐀-𝟿",
    character_category = "Mathematical notation symbols" -- ?
}

m["Zsym"] = {
    names = { "symbol" },
    characters = "─-➿←⇿⌀-⏳🌀-🛅",
    character_category = false, -- none
}

m["Zyyy"] = {
    names = { "undetermined" },
    -- This should not have any characters listed, probably
    character_category = false, -- none
}

m["Zzzz"] = {
    names = { "uncoded" },
    -- This should not have any characters listed
    character_category = false, -- none
}

return m
