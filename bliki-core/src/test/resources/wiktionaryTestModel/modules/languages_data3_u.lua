local u = mw.ustring.char

-- UTF-8 encoded strings for some commonly-used diacritics
local GRAVE     = u(0x0300)
local ACUTE     = u(0x0301)
local CIRC      = u(0x0302)
local TILDE     = u(0x0303)
local MACRON    = u(0x0304)
local BREVE     = u(0x0306)
local DOTABOVE  = u(0x0307)
local DIAER     = u(0x0308)
local CARON     = u(0x030C)
local DGRAVE    = u(0x030F)
local INVBREVE  = u(0x0311)
local DOTBELOW  = u(0x0323)
local RINGBELOW = u(0x0325)
local CEDILLA   = u(0x0327)

local m = {}

m["uam"] = {
    names = {"Uamué"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uan"] = {
    names = {"Kuan"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uar"] = {
    names = {"Tairuma"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uba"] = {
    names = {"Ubang"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bco",
}

m["ubi"] = {
    names = {"Ubi"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ubl"] = {
    names = {"Buhi'non Bikol"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ubr"] = {
    names = {"Ubir"},
    type = "regular",
    scripts = {"None"},
    family = "poz-ocw",
}

m["ubu"] = {
    names = {"Umbu-Ungu"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uby"] = {
    names = {"Ubykh"},
    type = "regular",
    scripts = {"Latn"},
    family = "cau-nwc",
}

m["uda"] = {
    names = {"Uda"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ude"] = {
    names = {"Udihe", "Udege", "Udekhe", "Udeghe"},
    type = "regular",
    scripts = {"Cyrl"},
    family = "tuw",
}

m["udg"] = {
    names = {"Muduga"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["udi"] = {
    names = {"Udi"},
    type = "regular",
    scripts = {"Cyrl", "Latn", "Armn"},
    family = "cau-nec",
}

m["udj"] = {
    names = {"Ujir"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["udl"] = {
    names = {"Uldeme"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["udm"] = {
    names = {"Udmurt"},
    type = "regular",
    scripts = {"Cyrl"},
    family = "fiu",
    translit_module = "udm-translit"}
m["udu"] = {
    names = {"Uduk"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ues"] = {
    names = {"Kioko"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ufi"] = {
    names = {"Ufim"},
    type = "regular",
    scripts = {"None"},
    family = "ngf-fin",
}

m["uga"] = {
    names = {"Ugaritic"},
    type = "regular",
    scripts = {"Ugar"},
    family = "sem-nwe",
}

m["ugb"] = {
    names = {"Kuku-Ugbanh"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uge"] = {
    names = {"Ughele"},
    type = "regular",
    scripts = {"None"},
    family = "poz-ocw",
}

m["ugn"] = {
    names = {"Ugandan Sign Language"},
    type = "regular",
    scripts = {"None"},
    family = "sgn",
}

m["ugo"] = {
    names = {"Ugong"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ugy"] = {
    names = {"Uruguayan Sign Language"},
    type = "regular",
    scripts = {"None"},
    family = "sgn",
}

m["uha"] = {
    names = {"Uhami"},
    type = "regular",
    scripts = {"Latn"},
    family = "alv-edo",
}

m["uhn"] = {
    names = {"Damal"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uis"] = {
    names = {"Uisai"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uiv"] = {
    names = {"Iyive"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bod",
}

m["uji"] = {
    names = {"Tanjijili"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uka"] = {
    names = {"Kaburi"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ukg"] = {
    names = {"Ukuriguma"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ukh"] = {
    names = {"Ukhwejo"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ukl"] = {
    names = {"Ukrainian Sign Language"},
    type = "regular",
    scripts = {"None"},
    family = "sgn",
}

m["ukp"] = {
    names = {"Ukpe-Bayobiri"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bco",
}

m["ukq"] = {
    names = {"Ukwa"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uks"] = {
    names = {"Kaapor Sign Language"},
    type = "regular",
    scripts = {"None"},
    family = "sgn",
}

m["uku"] = {
    names = {"Ukue"},
    type = "regular",
    scripts = {"Latn"},
    family = "alv-edo",
}

m["ukw"] = {
    names = {"Ukwuani-Aboh-Ndoni"},
    type = "regular",
    scripts = {"None"},
    family = "alv",
}

m["uky"] = {
    names = {"Kuuk Yak"},
    type = "regular",
    scripts = {"None"},
    family = "aus-psw",
}

m["ula"] = {
    names = {"Fungwa", "Ula", "Ura"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bco",
}

m["ulb"] = {
    names = {"Ulukwumi"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ulc"] = {
    names = {"Ulch"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ule"] = {
    names = {"Lule"},
    type = "regular",
    scripts = {"Latn"},
    family = "qfa-und",
}

m["ulf"] = {
    names = {"Afra"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uli"] = {
    names = {"Ulithian"},
    type = "regular",
    scripts = {"None"},
    family = "poz-mic",
}

m["ulk"] = {
    names = {"Meriam"},
    type = "regular",
    scripts = {"Latn"},
    family = "ngf",
}

m["ull"] = {
    names = {"Ullatan"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ulm"] = {
    names = {"Ulumanda'"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uln"] = {
    names = {"Unserdeutsch", "Rabaul Creole German"},
    type = "regular",
    scripts = {"Latn"},
    family = "crp",
    ancestors = {"de"},
}

m["ulu"] = {
    names = {"Uma' Lung"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ulw"] = {
    names = {"Ulwa"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uma"] = {
    names = {"Umatilla"},
    type = "regular",
    scripts = {"None"},
    family = "nai-shp",
}

m["umb"] = {
    names = {"Umbundu", "South Mbundu"},
    type = "regular",
    scripts = {"Latn"},
    family = "bnt",
}

m["umc"] = {
    names = {"Marrucinian"},
    type = "regular",
    scripts = {"Latn"},
    family = "itc",
}

m["umd"] = {
    names = {"Umbindhamu"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["umg"] = {
    names = {"Umbuygamu"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["umi"] = {
    names = {"Ukit"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["umm"] = {
    names = {"Umon"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bco",
}

m["umn"] = {
    names = {"Makyan Naga"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["umo"] = {
    names = {"Umotína"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ump"] = {
    names = {"Umpila"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["umr"] = {
    names = {"Umbugarla"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ums"] = {
    names = {"Pendau"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["umu"] = {
    names = {"Munsee"},
    type = "regular",
    scripts = {"Latn"},
    family = "del",
}

m["una"] = {
    names = {"North Watut"},
    type = "regular",
    scripts = {"None"},
    family = "poz-ocw",
}

m["und"] = {
    names = {"Undetermined"},
    type = "regular",
    scripts = {"Zyyy"},
    family = "qfa-not",
}

m["une"] = {
    names = {"Uneme"},
    type = "regular",
    scripts = {"Latn"},
    family = "alv-edo",
}

m["ung"] = {
    names = {"Ngarinyin"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["unk"] = {
    names = {"Enawené-Nawé"},
    type = "regular",
    scripts = {"None"},
    family = "awd",
}

m["unm"] = {
    names = {"Unami"},
    type = "regular",
    scripts = {"Latn"},
    family = "del",
    entry_name = {
        from = {"À", "Ä", "È", "Ë", "Ì", "Ò", "Ù", "à", "ä", "è", "ë", "ì", "ò", "ù"},
        to   = {"A", "A", "E", "E", "I", "O", "U", "a", "a", "e", "e", "i", "o", "u"}} ,
}

m["unn"] = {
    names = {"Kurnai", "Gunai", "Gaanay", "Ganai", "Gunnai'", "Kurnay"},
    type = "regular",
    scripts = {"Latn"},
    family = "aus-pam",
}

m["unr"] = {
    names = {"Mundari"},
    type = "regular",
    scripts = {"None"},
    family = "mun",
}

m["unu"] = {
    names = {"Unubahe"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["unx"] = {
    names = {"Munda"},
    type = "regular",
    scripts = {"None"},
    family = "mun",
}

m["unz"] = {
    names = {"Unde Kaili", "Banava", "Ndepuu", "West Kaili", "Lole", "Ganti"},
    type = "regular",
    scripts = {"Latn"},
    family = "poz-kal",
}

m["uok"] = {
    names = {"Uokha"},
    type = "regular",
    scripts = {"Latn"},
    family = "alv-edo",
}

m["upi"] = {
    names = {"Umeda"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["upv"] = {
    names = {"Uripiv-Wala-Rano-Atchin"},
    type = "regular",
    scripts = {"None"},
    family = "poz-vnc",
}

m["ura"] = {
    names = {"Urarina"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urb"] = {
    names = {"Urubú-Kaapor"},
    type = "regular",
    scripts = {"Latn"},
    family = "tup",
}

m["urc"] = {
    names = {"Urningangg"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ure"] = {
    names = {"Uru"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urf"] = {
    names = {"Uradhi"},
    type = "regular",
    scripts = {"Latn"},
    family = "aus-pam",
}

m["urg"] = {
    names = {"Urigina"},
    type = "regular",
    scripts = {"None"},
    family = "ngf",
}

m["urh"] = {
    names = {"Urhobo"},
    type = "regular",
    scripts = {"Latn"},
    family = "alv-edo",
}

m["uri"] = {
    names = {"Urim"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-tor",
}

m["urk"] = {
    names = {"Urak Lawoi'"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["url"] = {
    names = {"Urali"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urm"] = {
    names = {"Urapmin"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urn"] = {
    names = {"Uruangnirin"},
    type = "regular",
    scripts = {"Latn"},
    family = "plf",
}

m["uro"] = {
    names = {"Ura (New Guinea)", "Ura (Papua New Guinea)"},
    type = "regular",
    scripts = {"None"},
    family = "paa",
}

m["urp"] = {
    names = {"Uru-Pa-In"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urr"] = {
    names = {"Lehalurup"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urt"] = {
    names = {"Urat"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-tor",
}

m["uru"] = {
    names = {"Urumi"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urv"] = {
    names = {"Uruava"},
    type = "regular",
    scripts = {"None"},
    family = "poz-ocw",
}

m["urw"] = {
    names = {"Sop"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urx"] = {
    names = {"Urimo"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-tor",
}

m["ury"] = {
    names = {"Orya"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["urz"] = {
    names = {"Uru-Eu-Wau-Wau"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["usa"] = {
    names = {"Usarufa"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["ush"] = {
    names = {"Ushojo"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["usi"] = {
    names = {"Usui"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["usk"] = {
    names = {"Usaghade"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["usp"] = {
    names = {"Uspanteco"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["usu"] = {
    names = {"Uya"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uta"] = {
    names = {"Otank"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bod",
}

m["ute"] = {
    names = {"Ute", "Southern Paiute", "Colorado River Numic", "Chemehuevi"},
    type = "regular",
    scripts = {"Latn"},
    family = "azc-num",
}

m["utp"] = {
    names = {"Aba", "Amba", "Nebao", "Nembao"},
    type = "regular",
    scripts = {"Latn"},
    family = "poz-oce",
}

m["utr"] = {
    names = {"Etulo"},
    type = "regular",
    scripts = {"Latn"},
    family = "alv",
}

m["utu"] = {
    names = {"Utu"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uum"] = {
    names = {"Urum"},
    type = "regular",
    scripts = {"Cyrl"},
    family = "trk",
}

m["uun"] = {
    names = {"Kulon-Pazeh", "Pazeh", "Pazih", "Kulun", "Kulon"},
    type = "regular",
    scripts = {"Latn"},
    family = "map",
}

m["uur"] = {
    names = {"Ura (Vanuatu)"},
    type = "regular",
    scripts = {"None"},
    family = "poz-oce",
}

m["uuu"] = {
    names = {"U"},
    type = "regular",
    scripts = {"None"},
    family = "aav",
}

m["uve"] = {
    names = {"West Uvean"},
    type = "regular",
    scripts = {"None"},
    family = "poz-pol",
}

m["uvh"] = {
    names = {"Uri"},
    type = "regular",
    scripts = {"None"},
    family = "ngf-fin",
}

m["uvl"] = {
    names = {"Lote"},
    type = "regular",
    scripts = {"None"},
    family = "poz-ocw",
}

m["uwa"] = {
    names = {"Kuku-Uwanh"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uya"] = {
    names = {"Doko-Uyanga"},
    type = "regular",
    scripts = {"None"},
    family = "nic-bco",
}

m["uzn"] = {
    names = {"Northern Uzbek"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

m["uzs"] = {
    names = {"Southern Uzbek"},
    type = "regular",
    scripts = {"None"},
    family = "qfa-und",
}

return m
