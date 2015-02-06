local export = {}

-- romaji with diacritics to romaji without
local rd={
    ['ā']='aa',['ē']='ee',['ī']='ii',['ō']='ou',['ū']='uu'
};

-- equivalent hiragana = katakana pairs, h=k or hk
local hk={
    ['ぁ']='ァ',['あ']='ア',['ぃ']='ィ',['い']='イ',['ぅ']='ゥ',['う']='ウ',['ぇ']='ェ',['え']='エ',['ぉ']='ォ',['お']='オ',['か']='カ',['が']='ガ',['き']='キ',['ぎ']='ギ',['く']='ク',['ぐ']='グ',['け']='ケ',['げ']='ゲ',['こ']='コ',['ご']='ゴ',['さ']='サ',['ざ']='ザ',['し']='シ',['じ']='ジ',['す']='ス',['ず']='ズ',['せ']='セ',['ぜ']='ゼ',['そ']='ソ',['ぞ']='ゾ',['た']='タ',['だ']='ダ',['ち']='チ',['ぢ']='ヂ',['っ']='ッ',['つ']='ツ',['づ']='ヅ',['て']='テ',['で']='デ',['と']='ト',['ど']='ド',['な']='ナ',['に']='ニ',['ぬ']='ヌ',['ね']='ネ',['の']='ノ',['は']='ハ',['ば']='バ',['ぱ']='パ',['ひ']='ヒ',['び']='ビ',['ぴ']='ピ',['ふ']='フ',['ぶ']='ブ',['ぷ']='プ',['へ']='ヘ',['べ']='ベ',['ぺ']='ペ',['ほ']='ホ',['ぼ']='ボ',['ぽ']='ポ',['ま']='マ',['み']='ミ',['む']='ム',['め']='メ',['も']='モ',['ゃ']='ャ',['や']='ヤ',['ゅ']='ュ',['ゆ']='ユ',['ょ']='ョ',['よ']='ヨ',['ら']='ラ',['り']='リ',['る']='ル',['れ']='レ',['ろ']='ロ',['ゎ']='ヮ',['わ']='ワ',['ゐ']='ヰ',['ゑ']='ヱ',['を']='ヲ',['ん']='ン',['ゔ']='ヴ',['ゕ']='ヵ',['ゖ']='ヶ'
};

-- equivalent katakana = hiragana pairs, k=h or kh
local kh={
    ['ァ']='ぁ',['ア']='あ',['ィ']='ぃ',['イ']='い',['ゥ']='ぅ',['ウ']='う',['ェ']='ぇ',['エ']='え',['ォ']='ぉ',['オ']='お',['カ']='か',['ガ']='が',['キ']='き',['ギ']='ぎ',['ク']='く',['グ']='ぐ',['ケ']='け',['ゲ']='げ',['コ']='こ',['ゴ']='ご',['サ']='さ',['ザ']='ざ',['シ']='し',['ジ']='じ',['ス']='す',['ズ']='ず',['セ']='せ',['ゼ']='ぜ',['ソ']='そ',['ゾ']='ぞ',['タ']='た',['ダ']='だ',['チ']='ち',['ヂ']='ぢ',['ッ']='っ',['ツ']='つ',['ヅ']='づ',['テ']='て',['デ']='で',['ト']='と',['ド']='ど',['ナ']='な',['ニ']='に',['ヌ']='ぬ',['ネ']='ね',['ノ']='の',['ハ']='は',['バ']='ば',['パ']='ぱ',['ヒ']='ひ',['ビ']='び',['ピ']='ぴ',['フ']='ふ',['ブ']='ぶ',['プ']='ぷ',['ヘ']='へ',['ベ']='べ',['ペ']='ぺ',['ホ']='ほ',['ボ']='ぼ',['ポ']='ぽ',['マ']='ま',['ミ']='み',['ム']='む',['メ']='め',['モ']='も',['ャ']='ゃ',['ヤ']='や',['ュ']='ゅ',['ユ']='ゆ',['ョ']='ょ',['ヨ']='よ',['ラ']='ら',['リ']='り',['ル']='る',['レ']='れ',['ロ']='ろ',['ヮ']='ゎ',['ワ']='わ',['ヰ']='ゐ',['ヱ']='ゑ',['ヲ']='を',['ン']='ん',['ヴ']='ゔ',['ヵ']='ゕ',['ヶ']='ゖ'
};

-- equivalent katakana = romaji pairs, k=r or kr
-- clever trick: replaces ン with n@
local kr={
    ["ア"] = "a", ["イ"] = "i", ["イィ"] = "yi", ["イェ"] = "ye", ["ウ"] = "u", ["ヴ"] = "vu", ["ヴァ"] = "va", ["ヴィ"] = "vi", ["ヴィェ"] = "vye", ["ヴェ"] = "ve", ["ヴォ"] = "vo", ["ヴャ"] = "vya", ["ヴュ"] = "vyu", ["ヴョ"] = "vyo", ["ウァ"] = "wa", ["ウィ"] = "wi", ["ウゥ"] = "wu", ["ウェ"] = "we", ["ウォ"] = "wo", ["ウュ"] = "wyu", ["エ"] = "e", ["オ"] = "o", ["カ"] = "ka", ["キ"] = "ki", ["キェ"] = "kye", ["キャ"] = "kya", ["キュ"] = "kyu", ["キョ"] = "kyo", ["ガ"] = "ga", ["ギ"] = "gi", ["ギェ"] = "gye", ["ギャ"] = "gya", ["ギュ"] = "gyu", ["ギョ"] = "gyo", ["ク"] = "ku", ["クァ"] = "kwa", ["クィ"] = "kwi", ["クェ"] = "kwe", ["クォ"] = "kwo", ["クヮ"] = "kwa", ["グ"] = "gu", ["グァ"] = "gwa", ["グィ"] = "gwi", ["グェ"] = "gwe", ["グォ"] = "gwo", ["グヮ"] = "gwa", ["ケ"] = "ke", ["ゲ"] = "ge", ["コ"] = "ko", ["ゴ"] = "go", ["サ"] = "sa", ["ザ"] = "za", ["シ"] = "shi", ["シェ"] = "she", ["シャ"] = "sha", ["シュ"] = "shu", ["ショ"] = "sho", ["ジ"] = "ji", ["ジェ"] = "je", ["ジャ"] = "ja", ["ジュ"] = "ju", ["ジョ"] = "jo", ["ス"] = "su", ["スィ"] = "si", ["ズ"] = "zu", ["ズィ"] = "zi", ["セ"] = "se", ["ゼ"] = "ze", ["ソ"] = "so", ["ゾ"] = "zo", ["タ"] = "ta", ["ダ"] = "da", ["チ"] = "chi", ["チェ"] = "che", ["チャ"] = "cha", ["チュ"] = "chu", ["チョ"] = "cho", ["ヂ"] = "ji", ["ヂャ"] = "ja", ["ヂュ"] = "ju", ["ヂョ"] = "jo", ["ヅ"] = "zu", ["ツ"] = "tsu", ["ツァ"] = "tsa", ["ツィ"] = "tsi", ["ツェ"] = "tse", ["ツォ"] = "tso", ["ツュ"] = "tsyu", ["テ"] = "te", ["ティ"] = "ti", ["テュ"] = "tyu", ["デ"] = "de", ["ディ"] = "di", ["デュ"] = "dyu", ["ト"] = "to", ["トゥ"] = "tu", ["ド"] = "do", ["ドゥ"] = "du", ["ナ"] = "na", ["ニ"] = "ni", ["ニェ"] = "nye", ["ニャ"] = "nya", ["ニュ"] = "nyu", ["ニョ"] = "nyo", ["ヌ"] = "nu", ["ネ"] = "ne", ["ノ"] = "no", ["ハ"] = "ha", ["バ"] = "ba", ["パ"] = "pa", ["ヒ"] = "hi", ["ビ"] = "bi", ["ピ"] = "pi", ["ヒェ"] = "hye", ["ヒャ"] = "hya", ["ヒュ"] = "hyu", ["ヒョ"] = "hyo", ["ビェ"] = "bye", ["ピェ"] = "pye", ["ビャ"] = "bya", ["ピャ"] = "pya", ["ビュ"] = "byu", ["ピュ"] = "pyu", ["ビョ"] = "byo", ["ピョ"] = "pyo", ["フ"] = "fu", ["ファ"] = "fa", ["フィ"] = "fi", ["ブィ"] = "bi", ["フィェ"] = "fye", ["フェ"] = "fe", ["フォ"] = "fo", ["フャ"] = "fya", ["フュ"] = "fyu", ["フョ"] = "fyo", ["ブ"] = "bu", ["プ"] = "pu", ["ヘ"] = "he", ["ベ"] = "be", ["ペ"] = "pe", ["ホ"] = "ho", ["ボ"] = "bo", ["ポ"] = "po", ["ホゥ"] = "hu", ["マ"] = "ma", ["ミ"] = "mi", ["ミェ"] = "mye", ["ミャ"] = "mya", ["ミュ"] = "myu", ["ミョ"] = "myo", ["ム"] = "mu", ["メ"] = "me", ["モ"] = "mo", ["ヤ"] = "ya", ["ユ"] = "yu", ["ヨ"] = "yo", ["ラ"] = "ra", ["リ"] = "ri", ["ラ゜"] = "la", ["リ゜"] = "li", ["リェ"] = "rye", ["リャ"] = "rya", ["リュ"] = "ryu", ["リョ"] = "ryo", ["ル"] = "ru", ["ル゜"] = "lu", ["レ゜"] = "le", ["レ"] = "re", ["ロ"] = "ro", ["ロ゜"] = "lo", ["ワ"] = "wa", ["ヷ"] = "va", ["ヰ"] = "wi", ["ヸ"] = "vi", ["ヱ"] = "we", ["ヹ"] = "ve", ["ヲ"] = "o", ["ヺ"] = "vo", ["ン"] = "n@",
    ["、"] = ", ", ["。"] = ". ", ["・"] = " ", ["（"] = "(", ["）"] = ")", ["「"] = '"', ["」"] = '"', ["『"] = "'", ["』"] = "'", ["＝"] = "-", ["，"] = ", ", ["？"] = "?", ["："] = ": ",
    ["Ａ"] = "A", ["Ｂ"] = "B", ["Ｃ"] = "C", ["Ｄ"] = "D", ["Ｅ"] = "E", ["Ｆ"] = "F", ["Ｇ"] = "G", ["Ｈ"] = "H", ["Ｉ"] = "I", ["Ｊ"] = "J", ["Ｋ"] = "K", ["Ｌ"] = "L", ["Ｍ"] = "M", ["Ｎ"] = "N", ["Ｏ"] = "O", ["Ｐ"] = "P", ["Ｑ"] = "Q", ["Ｒ"] = "R", ["Ｓ"] = "S", ["Ｔ"] = "T", ["Ｕ"] = "U", ["Ｖ"] = "V", ["Ｗ"] = "W", ["Ｘ"] = "X", ["Ｙ"] = "Y", ["Ｚ"] = "Z",
    ["ａ"] = "a", ["ｂ"] = "b", ["ｃ"] = "c", ["ｄ"] = "d", ["ｅ"] = "e", ["ｆ"] = "f", ["ｇ"] = "g", ["ｈ"] = "h", ["ｉ"] = "i", ["ｊ"] = "j", ["ｋ"] = "k", ["ｌ"] = "l", ["ｍ"] = "m", ["ｎ"] = "n", ["ｏ"] = "o", ["ｐ"] = "p", ["ｑ"] = "q", ["ｒ"] = "r", ["ｓ"] = "s", ["t"] = "t", ["ｕ"] = "u", ["ｖ"] = "v", ["ｗ"] = "w", ["ｘ"] = "x", ["ｙ"] = "y", ["ｚ"] = "z",
    ["０"] = "0", ["１"] = "1", ["２"] = "2", ["３"] = "3", ["４"] = "4", ["５"] = "5", ["６"] = "6", ["７"] = "7", ["８"] = "8", ["９"] = "9"
};

-- equivalent romaji = katakana pairs, r=k or rk
local rk={
    ['wyu']='ウュ',['vyu']='ヴュ',['vyo']='ヴョ',['vye']='ヴィェ',['vya']='ヴャ',['tyu']='テュ',['ryu']='リュ',['ryo']='リョ',['rye']='リェ',['rya']='リャ',['pyu']='ピュ',['pyo']='ピョ',['pye']='ピェ',['pya']='ピャ',['nyu']='ニュ',['nyo']='ニョ',['nye']='ニェ',['nya']='ニャ',['myu']='ミュ',['myo']='ミョ',['mye']='ミェ',['mya']='ミャ',['kyu']='キュ',['kyo']='キョ',['kye']='キェ',['kya']='キャ',['kwo']='クォ',['kwi']='クィ',['kwe']='クェ',['kwa']='クァ',['kwa']='クヮ',['hyu']='ヒュ',['hyo']='ヒョ',['hye']='ヒェ',['hya']='ヒャ',['gyu']='ギュ',['gyo']='ギョ',['gye']='ギェ',['gya']='ギャ',['gwo']='グォ',['gwi']='グィ',['gwe']='グェ',['gwa']='グァ',['gwa']='グヮ',['fyu']='フュ',['fyo']='フョ',['fye']='フィェ',['fya']='フャ',['dyu']='デュ',['byu']='ビュ',['byo']='ビョ',['bye']='ビェ',['bya']='ビャ',['zu']='ズ',['zo']='ゾ',['zi']='ズィ',['ze']='ゼ',['za']='ザ',['yu']='ユ',['yo']='ヨ',['yi']='イィ',['ye']='イェ',['ya']='ヤ',['wu']='ウゥ',['wo']='ウォ',['wi']='ウィ',['we']='ウェ',['wa']='ワ',['vu']='ヴ',['vo']='ヴォ',['vi']='ヴィ',['ve']='ヴェ',['va']='ヴァ',['tu']='トゥ',['to']='ト',['ti']='ティ',['te']='テ',['ta']='タ',['su']='ス',['so']='ソ',['si']='スィ',['se']='セ',['sa']='サ',['ru']='ル',['ro']='ロ',['ri']='リ',['re']='レ',['ra']='ラ',['pu']='プ',['po']='ポ',['pi']='ピ',['pe']='ペ',['pa']='パ',['mu']='ム',['mo']='モ',['mi']='ミ',['me']='メ',['ma']='マ',['lu']='ル゜',['lo']='ロ゜',['li']='リ゜',['le']='レ゜',['la']='ラ゜',['ku']='ク',['ko']='コ',['ki']='キ',['ke']='ケ',['ka']='カ',['ju']='ジュ',['jo']='ジョ',['ji']='ジ',['je']='ジェ',['ja']='ジャ',['hu']='ホゥ',['ho']='ホ',['hi']='ヒ',['he']='ヘ',['ha']='ハ',['gu']='グ',['go']='ゴ',['gi']='ギ',['ge']='ゲ',['ga']='ガ',['fu']='フ',['fo']='フォ',['fi']='フィ',['fe']='フェ',['fa']='ファ',['du']='ドゥ',['do']='ド',['di']='ディ',['de']='デ',['da']='ダ',['bu']='ブ',['bo']='ボ',['bi']='ビ',['be']='ベ',['ba']='バ'
};

-- hiragana to empty
local hy={
    ['ぁ']='',['あ']='',['ぃ']='',['い']='',['ぅ']='',['う']='',['ぇ']='',['え']='',['ぉ']='',['お']='',['か']='',['が']='',['き']='',['ぎ']='',['く']='',['ぐ']='',['け']='',['げ']='',['こ']='',['ご']='',['さ']='',['ざ']='',['し']='',['じ']='',['す']='',['ず']='',['せ']='',['ぜ']='',['そ']='',['ぞ']='',['た']='',['だ']='',['ち']='',['ぢ']='',['っ']='',['つ']='',['づ']='',['て']='',['で']='',['と']='',['ど']='',['な']='',['に']='',['ぬ']='',['ね']='',['の']='',['は']='',['ば']='',['ぱ']='',['ひ']='',['び']='',['ぴ']='',['ふ']='',['ぶ']='',['ぷ']='',['へ']='',['べ']='',['ぺ']='',['ほ']='',['ぼ']='',['ぽ']='',['ま']='',['み']='',['む']='',['め']='',['も']='',['ゃ']='',['や']='',['ゅ']='',['ゆ']='',['ょ']='',['よ']='',['ら']='',['り']='',['る']='',['れ']='',['ろ']='',['ゎ']='',['わ']='',['ゐ']='',['ゑ']='',['を']='',['ん']='',['ゔ']='',['ゕ']='',['ゖ']=''
};

-- katakana to empty
local ky={
    ['ー']='',['ァ']='',['ア']='',['ィ']='',['イ']='',['ゥ']='',['ウ']='',['ェ']='',['エ']='',['ォ']='',['オ']='',['カ']='',['ガ']='',['キ']='',['ギ']='',['ク']='',['グ']='',['ケ']='',['ゲ']='',['コ']='',['ゴ']='',['サ']='',['ザ']='',['シ']='',['ジ']='',['ス']='',['ズ']='',['セ']='',['ゼ']='',['ソ']='',['ゾ']='',['タ']='',['ダ']='',['チ']='',['ヂ']='',['ッ']='',['ツ']='',['ヅ']='',['テ']='',['デ']='',['ト']='',['ド']='',['ナ']='',['ニ']='',['ヌ']='',['ネ']='',['ノ']='',['ハ']='',['バ']='',['パ']='',['ヒ']='',['ビ']='',['ピ']='',['フ']='',['ブ']='',['プ']='',['ヘ']='',['ベ']='',['ペ']='',['ホ']='',['ボ']='',['ポ']='',['マ']='',['ミ']='',['ム']='',['メ']='',['モ']='',['ャ']='',['ヤ']='',['ュ']='',['ユ']='',['ョ']='',['ヨ']='',['ラ']='',['リ']='',['ル']='',['レ']='',['ロ']='',['ヮ']='',['ワ']='',['ヰ']='',['ヱ']='',['ヲ']='',['ン']='',['ヴ']='',['ヵ']='',['ヶ']=''
};

-- Japanese abbreviation symbols to empty
local sy={
    ['々']='',['ゞ']=''
}

-- hiragana with dakuten to empty
local dakuten={
    ['が']='',['ぎ']='',['ぐ']='',['げ']='',['ご']='',['ざ']='',['じ']='',['ず']='',['ぜ']='',['ぞ']='',['だ']='',['ぢ']='',['づ']='',['で']='',['ど']='',['ば']='',['び']='',['ぶ']='',['べ']='',['ぼ']='',['ゔ']=''
}

-- hiragana with dakuten or handakuten to those without
local tenconv={
    ['が']='か',['ぎ']='き',['ぐ']='く',['げ']='け',['ご']='こ',['ざ']='さ',['じ']='し',['ず']='す',['ぜ']='せ',['ぞ']='そ',['だ']='た',['ぢ']='ち',['づ']='つ',['で']='て',['ど']='と',['ば']='は',['び']='ひ',['ぶ']='ふ',['べ']='へ',['ぼ']='ほ',['ぱ']='は',['ぴ']='ひ',['ぷ']='ふ',['ぺ']='へ',['ぽ']='ほ',['ゔ']='う'
}

-- hiragana with handakuten to empty
local handakuten={
    ['ぱ']='',['ぴ']='',['ぷ']='',['ぺ']='',['ぽ']=''
}

-- all small hiragana except small tsu (useful when counting morae)
local nonmora_to_empty={
    ['ぁ']='',['ぅ']='',['ぃ']='',['ぇ']='',['ぉ']='',['ゃ']='',['ゅ']='',['ょ']=''
}

local longvowels={
    ['あー']='ああ',['いー']='いい',['うー']='うう',['えー']='ええ',['おー']='おお',['ぁー']='ぁあ',['ぃー']='ぃい',['ぅー']='ぅう',['ぇー']='ぇえ',['ぉー']='ぉお', ['かー']='かあ',['きー']='きい',['くー']='くう',['けー']='けえ',['こー']='こお',['さー']='さあ',['しー']='しい',['すー']='すう',['せー']='せえ',['そー']='そお',['たー']='たあ',['ちー']='ちい',['つー']='つう',['てー']='てえ',['とー']='とお',['なー']='なあ',['にー']='にい',['ぬー']='ぬう',['ねー']='ねえ',['のー']='のお',['はー']='はあ',['ひー']='ひい',['ふー']='ふう',['へー']='へえ',['ほー']='ほお',['まー']='まあ',['みー']='みい',['むー']='むう',['めー']='めえ',['もー']='もお',['やー']='やあ',['ゆー']='ゆう',['よー']='よお',['ゃー']='ゃあ',['ゅー']='ゅう',['ょー']='ょお',['らー']='らあ',['りー']='りい',['るー']='るう',['れー']='れえ',['ろー']='ろお',['わー']='わあ'
}

local joyo_kanji = ([[
亜哀挨愛曖悪握圧扱宛嵐安案暗以衣位囲医依委威為畏胃尉異移萎偉椅彙意違維慰遺緯域育一
壱逸茨芋引印因咽姻員院淫陰飲隠韻右宇羽雨唄鬱畝浦運雲永泳英映栄営詠影鋭衛易疫益液駅悦越謁閲円延沿炎
宴怨媛援園煙猿遠鉛塩演縁艶汚王凹央応往押旺欧殴桜翁奥横岡屋億憶臆虞乙俺卸音恩温穏下化火加可仮何花佳
価果河苛科架夏家荷華菓貨渦過嫁暇禍靴寡歌箇稼課蚊牙瓦我画芽賀雅餓介回灰会快戒改怪拐悔海界皆械絵開階
塊楷解潰壊懐諧貝外劾害崖涯街慨蓋該概骸垣柿各角拡革格核殻郭覚較隔閣確獲嚇穫学岳楽額顎掛潟括活喝渇割
葛滑褐轄且株釜鎌刈干刊甘汗缶完肝官冠巻看陥乾勘患貫寒喚堪換敢棺款間閑勧寛幹感漢慣管関歓監緩憾還館環
簡観韓艦鑑丸含岸岩玩眼頑顔願企伎危机気岐希忌汽奇祈季紀軌既記起飢鬼帰基寄規亀喜幾揮期棋貴棄毀旗器畿
輝機騎技宜偽欺義疑儀戯擬犠議菊吉喫詰却客脚逆虐九久及弓丘旧休吸朽臼求究泣急級糾宮救球給嗅窮牛去巨居
拒拠挙虚許距魚御漁凶共叫狂京享供協況峡挟狭恐恭胸脅強教郷境橋矯鏡競響驚仰暁業凝曲局極玉巾斤均近金菌
勤琴筋僅禁緊錦謹襟吟銀区句苦駆具惧愚空偶遇隅串屈掘窟熊繰君訓勲薫軍郡群兄刑形系径茎係型契計恵啓掲渓
経蛍敬景軽傾携継詣慶憬稽憩警鶏芸迎鯨隙劇撃激桁欠穴血決結傑潔月犬件見券肩建研県倹兼剣拳軒健険圏堅検
嫌献絹遣権憲賢謙鍵繭顕験懸元幻玄言弦限原現舷減源厳己戸古呼固孤弧股虎故枯個庫湖雇誇鼓錮顧五互午呉後
娯悟碁語誤護口工公勾孔功巧広甲交光向后好江考行坑孝抗攻更効幸拘肯侯厚恒洪皇紅荒郊香候校耕航貢降高康
控梗黄喉慌港硬絞項溝鉱構綱酵稿興衡鋼講購乞号合拷剛傲豪克告谷刻国黒穀酷獄骨駒込頃今困昆恨根婚混痕紺
魂墾懇左佐沙査砂唆差詐鎖座挫才再災妻采砕宰栽彩採済祭斎細菜最裁債催塞歳載際埼在材剤財罪崎作削昨柵索
策酢搾錯咲冊札刷刹拶殺察撮擦雑皿三山参桟蚕惨産傘散算酸賛残斬暫士子支止氏仕史司四市矢旨死糸至伺志私
使刺始姉枝祉肢姿思指施師恣紙脂視紫詞歯嗣試詩資飼誌雌摯賜諮示字寺次耳自似児事侍治持時滋慈辞磁餌璽鹿
式識軸七叱失室疾執湿嫉漆質実芝写社車舎者射捨赦斜煮遮謝邪蛇尺借酌釈爵若弱寂手主守朱取狩首殊珠酒腫種
趣寿受呪授需儒樹収囚州舟秀周宗拾秋臭修袖終羞習週就衆集愁酬醜蹴襲十汁充住柔重従渋銃獣縦叔祝宿淑粛縮
塾熟出述術俊春瞬旬巡盾准殉純循順準潤遵処初所書庶暑署緒諸女如助序叙徐除小升少召匠床抄肖尚招承昇松沼
昭宵将消症祥称笑唱商渉章紹訟勝掌晶焼焦硝粧詔証象傷奨照詳彰障憧衝賞償礁鐘上丈冗条状乗城浄剰常情場畳
蒸縄壌嬢錠譲醸色拭食植殖飾触嘱織職辱尻心申伸臣芯身辛侵信津神唇娠振浸真針深紳進森診寝慎新審震薪親人
刃仁尽迅甚陣尋腎須図水吹垂炊帥粋衰推酔遂睡穂随髄枢崇数据杉裾寸瀬是井世正生成西声制姓征性青斉政星牲
省凄逝清盛婿晴勢聖誠精製誓静請整醒税夕斥石赤昔析席脊隻惜戚責跡積績籍切折拙窃接設雪摂節説舌絶千川仙
占先宣専泉浅洗染扇栓旋船戦煎羨腺詮践箋銭潜線遷選薦繊鮮全前善然禅漸膳繕狙阻祖租素措粗組疎訴塑遡礎双
壮早争走奏相荘草送倉捜挿桑巣掃曹曽爽窓創喪痩葬装僧想層総遭槽踪操燥霜騒藻造像増憎蔵贈臓即束足促則息
捉速側測俗族属賊続卒率存村孫尊損遜他多汰打妥唾堕惰駄太対体耐待怠胎退帯泰堆袋逮替貸隊滞態戴大代台第
題滝宅択沢卓拓託濯諾濁但達脱奪棚誰丹旦担単炭胆探淡短嘆端綻誕鍛団男段断弾暖談壇地池知値恥致遅痴稚置
緻竹畜逐蓄築秩窒茶着嫡中仲虫沖宙忠抽注昼柱衷酎鋳駐著貯丁弔庁兆町長挑帳張彫眺釣頂鳥朝貼超腸跳徴嘲潮
澄調聴懲直勅捗沈珍朕陳賃鎮追椎墜通痛塚漬坪爪鶴低呈廷弟定底抵邸亭貞帝訂庭逓停偵堤提程艇締諦泥的笛摘
滴適敵溺迭哲鉄徹撤天典店点展添転塡田伝殿電斗吐妬徒途都渡塗賭土奴努度怒刀冬灯当投豆東到逃倒凍唐島桃
討透党悼盗陶塔搭棟湯痘登答等筒統稲踏糖頭謄藤闘騰同洞胴動堂童道働銅導瞳峠匿特得督徳篤毒独読栃凸突届
屯豚頓貪鈍曇丼那奈内梨謎鍋南軟難二尼弐匂肉虹日入乳尿任妊忍認寧熱年念捻粘燃悩納能脳農濃把波派破覇馬
婆罵拝杯背肺俳配排敗廃輩売倍梅培陪媒買賠白伯拍泊迫剝舶博薄麦漠縛爆箱箸畑肌八鉢発髪伐抜罰閥反半氾犯
帆汎伴判坂阪板版班畔般販斑飯搬煩頒範繁藩晩番蛮盤比皮妃否批彼披肥非卑飛疲秘被悲扉費碑罷避尾眉美備微
鼻膝肘匹必泌筆姫百氷表俵票評漂標苗秒病描猫品浜貧賓頻敏瓶不夫父付布扶府怖阜附訃負赴浮婦符富普腐敷膚
賦譜侮武部舞封風伏服副幅復福腹複覆払沸仏物粉紛雰噴墳憤奮分文聞丙平兵併並柄陛閉塀幣弊蔽餅米壁璧癖別
蔑片辺返変偏遍編弁便勉歩保哺捕補舗母募墓慕暮簿方包芳邦奉宝抱放法泡胞俸倣峰砲崩訪報蜂豊飽褒縫亡乏忙
坊妨忘防房肪某冒剖紡望傍帽棒貿貌暴膨謀頰北木朴牧睦僕墨撲没勃堀本奔翻凡盆麻摩磨魔毎妹枚昧埋幕膜枕又
末抹万満慢漫未味魅岬密蜜脈妙民眠矛務無夢霧娘名命明迷冥盟銘鳴滅免面綿麺茂模毛妄盲耗猛網目黙門紋問冶
夜野弥厄役約訳薬躍闇由油喩愉諭輸癒唯友有勇幽悠郵湧猶裕遊雄誘憂融優与予余誉預幼用羊妖洋要容庸揚揺葉
陽溶腰様瘍踊窯養擁謡曜抑沃浴欲翌翼拉裸羅来雷頼絡落酪辣乱卵覧濫藍欄吏利里理痢裏履璃離陸立律慄略柳流
留竜粒隆硫侶旅虜慮了両良料涼猟陵量僚領寮療瞭糧力緑林厘倫輪隣臨瑠涙累塁類令礼冷励戻例鈴零霊隷齢麗暦
歴列劣烈裂恋連廉練錬呂炉賂路露老労弄郎朗浪廊楼漏籠六録麓論和話賄脇惑枠湾腕]]):gsub("[\n\t ]", "")

local grade1 = ([[一右雨円王音下火花貝学気九休玉金空月犬見五口校左三山子四糸字耳七車手十出女小上森
人水正生生夕石赤千川先早草足村大男竹中虫町天田土二日入年白八百文木本名目立力林六
]]):gsub("[\n\t ]", "")

local grade2 = ([[引羽雲園遠何科夏家歌画回会海絵外角楽活間丸岩顔汽記帰弓牛魚京強教近兄形計元言原戸
古午後語工公広交光考行高黄合谷国黒今才細作算止市矢姉思紙寺自時室社弱首秋週春書少場色食心新親図数西
声星晴切雪船線前組走多太体台地池知茶昼長鳥朝直通弟店点電刀冬当東答頭同道読内南肉馬売買麦半番父風分
聞米歩母方北毎妹万明鳴毛門夜野友用曜来里理話
]]):gsub("[\n\t ]", "")

local grade3 = ([[丁世両主乗予事仕他代住使係倍全具写列助勉動勝化区医去反取受号向君味命和品員商問坂
央始委守安定実客宮宿寒対局屋岸島州帳平幸度庫庭式役待急息悪悲想意感所打投拾持指放整旅族昔昭暑暗曲有
服期板柱根植業様横橋次歯死氷決油波注泳洋流消深温港湖湯漢炭物球由申界畑病発登皮皿相県真着短研礼神祭
福秒究章童笛第筆等箱級終緑練羊美習者育苦荷落葉薬血表詩調談豆負起路身転軽農返追送速進遊運部都配酒重
鉄銀開院陽階集面題飲館駅鼻
]]):gsub("[\n\t ]", "")

local grade4 = ([[不争付令以仲伝位低例便信倉候借停健側働億兆児共兵典冷初別利刷副功加努労勇包卒協単
博印参史司各告周唱喜器囲固型堂塩士変夫失好季孫完官害察巣差希席帯底府康建径徒得必念愛成戦折挙改救敗
散料旗昨景最望未末札材束松果栄案梅械極標機欠歴残殺毒氏民求治法泣浅浴清満漁灯無然焼照熱牧特産的省祝
票種積競笑管節粉紀約結給続置老胃脈腸臣航良芸芽英菜街衣要覚観訓試説課議象貨貯費賞軍輪辞辺連達選郡量
録鏡関陸隊静順願類飛飯養験
]]):gsub("[\n\t ]", "")

local grade5 = ([[久仏仮件任似余価保修俵個備像再刊判制券則効務勢厚句可営因団圧在均基報境墓増夢妻婦
容寄富導居属布師常幹序弁張往復徳志応快性恩情態慣承技招授採接提損支政故敵断旧易暴条枝査格桜検構武比
永河液混減測準演潔災燃版犯状独率現留略益眼破確示祖禁移程税築精素経統絶綿総編績織罪群義耕職肥能興舌
舎術衛製複規解設許証評講謝識護豊財貧責貸貿賀資賛質輸述迷退逆造過適酸鉱銅銭防限険際雑非預領額飼
]]):gsub("[\n\t ]", "")

local grade6 = ([[並乱乳亡仁供俳値傷優党冊処刻割創劇勤危卵厳収后否吸呼善困垂城域奏奮姿存孝宅宇宗宙
宝宣密寸専射将尊就尺届展層己巻幕干幼庁座延律従忘忠憲我批担拝拡捨探推揮操敬映晩暖暮朗机枚染株棒模権
樹欲段沿泉洗派済源潮激灰熟片班異疑痛皇盛盟看砂磁私秘穀穴窓筋策簡糖系紅納純絹縦縮署翌聖肺背胸脳腹臓
臨至若著蒸蔵蚕衆裁装裏補視覧討訪訳詞誌認誕誠誤論諸警貴賃遺郵郷針鋼閉閣降陛除障難革頂骨
]]):gsub("[\n\t ]", "")

local secondary = ([[堀撃茂羅匂誇斉袋弊沃随崎逐漂枕且抗揺涙鐘鮮沢洞怠嬢嚇焦喩淡被般捜頰岳疲侵廷眺
稼唾塀霊迅附醜屈棋坊珍恐賓筒苗摘椎寝軟絞凡斑悦勧耐緩蔽坪沼衰譲柵蹴拷慶替愁繁皆覆雅沈踏疎継扶朕隔舗
妖粘喫炊抜賄悠弥腰崩倫循是阜脱掲舷冶紹沸頻押疾寂雄扇臆恋俊嫌乏契傾迎竜盤触硫括惰併滴凶墨俺鹿巾碁儀
訂袖弄箇堆贈踊萎碑褐騒井弔怖沙甘慨芝剛准玄股趣控販妬餅阪錬枯搾稲伴添抽鬼尾壁床滅轄拠繊拍幣掌惑腐埼
漏核奥棺虚譜嗣鉢潤寧姫陪鋭濯壱慰跳該靴症偶浸姻穫響澄尉蚊鈍鎌圏頑既又窒屯肖宜貞冠帥蛇欧仰宛煮伯撮伺
紳徴呈吹麺稽桁秀吉狂湿柄栓胆呉克廊双郊塑駐啓拒繕柔捗闘酎剝潟遡陥霧摩巨腫邦召杯購媒畳荒陵膨裾糾脂超
升丈芋禍伎麓儒錯梗丘媛甚膝猫娠隣閥罵符披洪瀬剰騰如琴猶徹錮紺頼弾巡廃隻嗅壇籠請縄凍詐励痕醸忙苛凝遜
寡詠監酢諧僧伐爽浪臭硬賠叱痩襲践匿矯培詣墾槽謁塁憂載越戴吏挑渓醒柿耗殖勘幅喚墜吟羞謡犠津撤輩喪催侮
鬱誰獲喉庸戻悼尿噴璃蓄遇錠杉雇緊免壌菌卑拐駒隆塗彙鉛胎惜江桑陣嫉顧懐彩鈴腎摯陳懲措遷茨軸旺辣彰突謀
怪唄覇爆漠箋傍餌鶏与抑頒采冗痘隙掛尚肝貢旨匠垣囚昇憧涯抱瞳砲舶漸殻携藻祈嫁塡濃遂浄懇婆廉酌虎韓恒畔
剖仙換豚慕乞嫡占泥艦咽叫呪郭逓緯普柳排赴酔艶曹飽兼幾威較薄脇渉促刑鼓呂傘丙斎猛逝膜肢斤慌称羨挫貪憬
唯宰勲舟香丹栽毀裂奇鍋賊遍畏斥枢懸乙鍛衝釜滞珠督飢履藤諦薦恭蛮扱佳痴酷拶虹裕捻哺偽臼畿盾蜂暇唆須粋
融窃礎閑吐娯麗崖妙粛依寛窟伏那汚環憩怨貫瞭芯畜綱墳露宵訴隷逸虞丼奈熊曇蓋憶忍煎淑青詔盗謎虜牲簿跡瑠
齢剤浦秩恨迭端慮汰泡褒房雷凹濫栃豪抄渇愉尋殊酬蔑岐頓溺娘倒肘輝亜訟紡倣愚滋艇昧罰梨含駆婚戯祉邸尻窯
朽稿憾哀桃及魅髄奉烈菊雌穏軌捕煙妥葛揚韻絡峠遣璧湧旦煩磨錦楼泰鍵裸湾衷享婿妃謄擦炉躍狙唐俸項戚溝蜜
岡唇到悩侶薫爪亭漬逮膳串抵綻刺殿擁療勅獄累慈旋削蛍恣描顎嵐姓癒霜孔俗驚滝潜塞峡即釣孤析奨淫華遮却執
惨砕緻款曽彼寿塾封翁厄震紋把瘍伸炎餓互沖紛帝遵挟挨魂患弧峰妨紫瞬振黙厘爵殴閲倹拭攻帽込猿更汁肪範賦
偉釈礁忌郎鑑乾払叔託胞締離篤瓦衡宴髪繭壮甲盲援罷瓶凸幽翼飾征銃肩禅漆殉刈脊茎誓途粗怒隅脚喝悟欄御劣
索冥堤粧弦玩暫維搬肯献辱芳傲缶狭麻施尼鶴濁握勃但介溶藍猟虐拓慢塔晶叙浮距棟傑敢燥惧糧睦審奪葬軒据繰
拳戒聴腺企亀訃酪脅還渋妊貌透悔謙誘縛膚駄隠稚詰縫盆鋳鎮朱網欺架匹骸阻赦楷貼没泊充棄捉泌偏坑緒腕敏汗
拘畝賜桟睡剣痢祥逃募擬寮影雰詳酵胴諭幻疫避暁需掃踪昆誉挿刃騎弐概侯斗椅辛舞眠僚頃渦硝汎堪眉嘲債薪抹
璽朴岬摂僅慄闇拉埋塊侍肌滑佐菓賢僕奔癖拙荘矛塚違浜租診巧狩迫賭詮潰冒奴陶卸翻氾彫縁況堅妄崇了遭鎖帆
徐粒堕銘掘斜償択渡枠歓憤謹暦漫邪搭斬窮嘱撲偵庶刹箸為尽涼劾魔賂遅凄憎扉穂壊籍嘆某卓顕曖牙棚襟微獣陰
恥鯨慎旬諮歳哲恵致敷零藩諾咲勾
]]):gsub("[\n\t ]", "")

local jinmeiyo_kanji = ([[
丑丞乃之乎也云亘亙些亦亥亨亮仔伊伍伽佃佑伶侃侑俄俠俣俐倭俱倦倖偲傭儲允兎兜其冴凌凜凛凧凪凰凱函劉劫
勁勺勿匁匡廿卜卯卿厨厩叉叡叢叶只吾吞吻哉哨啄哩喬喧喰喋嘩嘉嘗噌噂圃圭坐尭堯坦埴堰堺堵塙壕壬夷奄奎套
娃姪姥娩嬉孟宏宋宕宥寅寓寵尖尤屑峨峻崚嵯嵩嶺巌巖已巳巴巷巽帖幌幡庄庇庚庵廟廻弘弛彗彦彪彬徠忽怜恢恰
恕悌惟惚悉惇惹惺惣慧憐戊或戟托按挺挽掬捲捷捺捧掠揃摑摺撒撰撞播撫擢孜敦斐斡斧斯於旭昂昊昏昌昴晏晃晄
晒晋晟晦晨智暉暢曙曝曳朋朔杏杖杜李杭杵杷枇柑柴柘柊柏柾柚桧檜栞桔桂栖桐栗梧梓梢梛梯桶梶椛梁棲椋椀楯
楚楕椿楠楓椰楢楊榎樺榊榛槙槇槍槌樫槻樟樋橘樽橙檎檀櫂櫛櫓欣欽歎此殆毅毘毬汀汝汐汲沌沓沫洸洲洵洛浩浬
淵淳渚渚淀淋渥湘湊湛溢滉溜漱漕漣澪濡瀕灘灸灼烏焰焚煌煤煉熙燕燎燦燭燿爾牒牟牡牽犀狼猪猪獅玖珂珈珊珀
玲琢琢琉瑛琥琶琵琳瑚瑞瑶瑳瓜瓢甥甫畠畢疋疏皐皓眸瞥矩砦砥砧硯碓碗碩碧磐磯祇祢禰祐祐祷禱禄祿禎禎禽禾
秦秤稀稔稟稜穣穰穹穿窄窪窺竣竪竺竿笈笹笙笠筈筑箕箔篇篠簞簾籾粥粟糊紘紗紐絃紬絆絢綺綜綴緋綾綸縞徽繫
繡纂纏羚翔翠耀而耶耽聡肇肋肴胤胡脩腔脹膏臥舜舵芥芹芭芙芦苑茄苔苺茅茉茸茜莞荻莫莉菅菫菖萄菩萌萠萊菱
葦葵萱葺萩董葡蓑蒔蒐蒼蒲蒙蓉蓮蔭蔣蔦蓬蔓蕎蕨蕉蕃蕪薙蕾蕗藁薩蘇蘭蝦蝶螺蟬蟹蠟衿袈袴裡裟裳襖訊訣註詢
詫誼諏諄諒謂諺讃豹貰賑赳跨蹄蹟輔輯輿轟辰辻迂迄辿迪迦這逞逗逢遥遙遁遼邑祁郁鄭酉醇醐醍醬釉釘釧銑鋒鋸
錘錐錆錫鍬鎧閃閏閤阿陀隈隼雀雁雛雫霞靖鞄鞍鞘鞠鞭頁頌頗顚颯饗馨馴馳駕駿驍魁魯鮎鯉鯛鰯鱒鱗鳩鳶鳳鴨鴻
鵜鵬鷗鷲鷺鷹麒麟麿黎黛鼎]]):gsub("[\n\t ]", "")

export.data = {
    joyo_kanji = joyo_kanji,
    jinmeiyo_kanji = jinmeiyo_kanji,
    grade1 = grade1,
    grade2 = grade2,
    grade3 = grade3,
    grade4 = grade4,
    grade5 = grade5,
    grade6 = grade6
}

function export.romaji_dediacritics(text)
    if type(text) == "table" then
        text = text.args[1]
    end
    return (mw.ustring.gsub(text, '.', rd))
end

function export.hira_to_kata(text)
    if type(text) == "table" then
        text = text.args[1]
    end
    return (mw.ustring.gsub(text, '.', hk))
end

function export.kata_to_hira(text)
    if type(text) == "table" then
        text = text.args[1]
    end
    return (mw.ustring.gsub(text, '.', kh))
end

function export.kana_to_romaji(text, nodiacr)
    if type(text) == "table" then -- assume a frame.
    text, nodiacr = text.args[1], text.args[2]
    end

    local kr_minus_period = kr
    kr_minus_period["。"] = "。"

    -- convert Japanese spaces to western spaces
    text = mw.ustring.gsub(text, '　', ' ')

    -- if there is a は separated by halfwidth spaces, romanize it as " wa "
    text = mw.ustring.gsub(text, ' は ', ' wa ')
    -- also if it follows a space and is the last character, e.g. それでは
    text = mw.ustring.gsub(text, ' は$', ' wa')
    -- or " は、"
    text = mw.ustring.gsub(text, ' は、', ' wa,')
    -- or " は。"
    text = mw.ustring.gsub(text, ' は。', ' wa. ')
    text = mw.ustring.gsub(text, ' は？', ' wa? ')
    text = mw.ustring.gsub(text, ' は）', ' wa)')
    -- or " '''は''' "
    text = mw.ustring.gsub(text, " '''は''' ", " '''wa''' ")
    -- romanize では as "dewa"
    text = mw.ustring.gsub(text, ' では ', ' dewa ')
    text = mw.ustring.gsub(text, ' では$', ' dewa')
    text = mw.ustring.gsub(text, ' では、', ' dewa,')
    text = mw.ustring.gsub(text, ' では。', ' dewa. ')
    text = mw.ustring.gsub(text, ' では？', ' dewa? ')
    text = mw.ustring.gsub(text, ' では）', ' dewa)')
    text = mw.ustring.gsub(text, "'''では'''", "'''dewa'''")
    -- romanize で は (with space) as "de wa"
    text = mw.ustring.gsub(text, "'''で は'''", "'''de wa'''")
    -- same sort of thing for へ
    text = mw.ustring.gsub(text, ' へ ', ' e ')
    text = mw.ustring.gsub(text, " '''へ''' ", " '''e''' ")
    text = mw.ustring.gsub(text, ' へ$', ' e')
    text = mw.ustring.gsub(text, ' へ、', ' e,')
    text = mw.ustring.gsub(text, ' へ。', ' e. ')
    text = mw.ustring.gsub(text, ' へ？', ' e?')
    text = mw.ustring.gsub(text, ' へ）', ' e)')
    -- dangling small tsu is romanized as nothing
    text = mw.ustring.gsub(text, 'ッ。', '。')

    -- ゝ means "repeat the previous character" and is used with hiragana, like 々 is for kanji
    -- TODO: do same sort of thing for ゞ
    text = mw.ustring.gsub(text, '(.)ゝ', '%1%1')

    -- convert hiragana to katakana
    text = mw.ustring.gsub(text, '.', hk)
    -- replace katakana with romaji (?? not sure what the pattern below does ??)
    -- this is hackish, but we're using the period to indicate morpheme boundaries to prevent macrons
    -- from forming across them, so we'll remove the ASCII periods used for markup but not the Japanese periods

    -- convert the Japanese periods at the end
    --table.remove(kr_minus_period, "。")
    text = mw.ustring.gsub(text, '.[ィェォャュョァヮゥ゜]?ェ?', kr_minus_period)

    -- replace long vowel mark with the vowel that comes before
    text = mw.ustring.gsub(text, '([aeiou])ー', '%1%1')

    -- add vowels with diacritics
    if not nodiacr then
        text = mw.ustring.gsub(text, 'oo', 'ō')
        text = mw.ustring.gsub(text, 'aa', 'ā')
        text = mw.ustring.gsub(text, 'ee', 'ē')
        text = mw.ustring.gsub(text, 'ou', 'ō')
        text = mw.ustring.gsub(text, 'uu', 'ū')
        text = mw.ustring.gsub(text, 'ii', 'ī')
    end

    -- if input had spaces, keep them
    -- if the input string had periods, then remove them now
    text = mw.ustring.gsub(text, '%.', '')
    -- now that markup periods are gone, convert the Japanese periods to western periods
    text = mw.ustring.gsub(text, "。", ". ")

    -- romanize sokuon or geminate consonants
    -- text = mw.ustring.gsub(text, '^ッ', '')
    -- double the previous consonant letter if there is a small tsu
    text = mw.ustring.gsub(text, 'ッ([kstpgdbjzrfh])', '%1%1')
    -- replace ッc with tc
    text = mw.ustring.gsub(text, 'ッc', 'tc')
    -- if small tsu comes at the end, just throw it away
    text = mw.ustring.gsub(text, 'ッ$', '')

    -- the @ is used to determine when to insert an opostrophe after ん or ン
    -- (all is kata at that point)
    -- insert apostrophe when ン is followed by a vowel or
    -- y, which corresponds to the cases んや (n'ya) んゆ (n'yu) and んよ (n'yo)
    text = mw.ustring.gsub(text, "@([aeiouāēīōūy])", "'%1")
    -- remove @
    text = mw.ustring.gsub(text, "@", "")

    -- capitalize any letter following a ^ symbol
    text = mw.ustring.gsub(text, "%^%l", mw.ustring.upper)
    -- remove ^
    text = mw.ustring.gsub(text, "%^", "")

    return text
end

-- removes spaces and hyphens from input
-- intended to be used when checking manual romaji to allow the
-- insertion of spaces or hyphens in manual romaji without appearing "wrong"
function export.rm_spaces_hyphens(f)
    local text = f.args[1]
    text = mw.ustring.gsub(text, ' ', '')
    text = mw.ustring.gsub(text, '-', '')
    text = mw.ustring.gsub(text, '%.', '')
    text = mw.ustring.gsub(text, '&nbsp;', '')
    text = mw.ustring.gsub(text, '\'', '')
    return text
end

function export.romaji_to_kata(f)
    local text = f.args[1]
    text = mw.ustring.gsub(text, '.', rd)
    text = mw.ustring.gsub(text, 'kk', 'ッk')
    text = mw.ustring.gsub(text, 'ss', 'ッs')
    text = mw.ustring.gsub(text, 'tt', 'ッt')
    text = mw.ustring.gsub(text, 'pp', 'ッp')
    text = mw.ustring.gsub(text, 'bb', 'ッb')
    text = mw.ustring.gsub(text, 'dd', 'ッd')
    text = mw.ustring.gsub(text, 'gg', 'ッg')
    text = mw.ustring.gsub(text, 'jj', 'ッj')
    text = mw.ustring.gsub(text, 'tc', 'ッc')
    text = mw.ustring.gsub(text, 'tsyu', 'ツュ')
    text = mw.ustring.gsub(text, 'ts[uoiea]', {['tsu']='ツ',['tso']='ツォ',['tsi']='ツィ',['tse']='ツェ',['tsa']='ツァ'})
    text = mw.ustring.gsub(text, 'sh[uoiea]', {['shu']='シュ',['sho']='ショ',['shi']='シ',['she']='シェ',['sha']='シャ'})
    text = mw.ustring.gsub(text, 'ch[uoiea]', {['chu']='チュ',['cho']='チョ',['chi']='チ',['che']='チェ',['cha']='チャ'})
    text = mw.ustring.gsub(text, "n[uoiea']?", {['nu']='ヌ',['no']='ノ',['ni']='ニ',['ne']='ネ',['na']='ナ',['n']='ン',["n'"]='ン'})
    text = mw.ustring.gsub(text, '[wvtrpsmlkjhgfdbzy][yw]?[uoiea]', rk)
    text = mw.ustring.gsub(text, 'u', 'ウ')
    text = mw.ustring.gsub(text, 'o', 'オ')
    text = mw.ustring.gsub(text, 'i', 'イ')
    text = mw.ustring.gsub(text, 'e', 'エ')
    text = mw.ustring.gsub(text, 'a', 'ア')
    return text
end

-- expects: any mix of kanji and kana
-- determines the script types used
-- e.g. given イギリス人, it returns Kana+Hani
function export.script(f)
    text, script = type(f) == 'table' and f.args[1] or f, {}

    if mw.ustring.match(text, '[ぁ-ゖ]') then
        table.insert(script, 'Hira')
    end
    -- TODO: there are two kanas.  This should insert Kata.
    if mw.ustring.match(text, '[ァ-ヺ]') then
        table.insert(script, 'Kana')
    end
    -- 一 is unicode 4e00, previously used 丁 is 4e01
    if mw.ustring.match(text, '[一-龯㐀-䶵]') then
        table.insert(script, 'Hani')
    end
    -- matching %a should have worked but matched the end of every string
    if mw.ustring.match(text, '[a-zA-ZāēīōūĀĒĪŌŪａ-ｚＡ-Ｚ]') then
        table.insert(script, 'Romaji')
    end
    if mw.ustring.match(text, '[0-9０-９]') then
        table.insert(script, 'Number')
    end
    if mw.ustring.match(text, '[〆々]') then
        table.insert(script, 'Abbreviation')
    end

    return table.concat(script, '+')
end

-- accepts the entry name, extracts the kanji, and
-- puts the kanji inside {{ja-kanji|}} and returns it
function export.extract_kanji(f)
    local text = f.args[1]
    local len = 1
    local result = ''
    text = mw.ustring.gsub(mw.ustring.gsub(text, ".", ky), ".", hy)
    text = mw.ustring.gsub(text,' ','')
    len = mw.ustring.len(text)
    if text ~= '' then
        result = '{{ja-kanjitab'
        --		for i = 1, len, 1 do
        --			char = mw.ustring.sub(text,i,i)
        --			result = (result .. '|' .. char)
        --		end
        result = (result .. '}}')
    end
    return result
end

-- returns the number of kanji in this term
function export.count_kanji(f)
    local text = f.args[1]
    local len = 1
    -- replace 時々 with 時時
    text = mw.ustring.gsub(text, '([一-龯㐀-䶵])々', '%1%1')
    -- first and last characters in unicode CJK unified ideographs block, see
    -- List_of_CJK_Unified_Ideographs,_part_1_of_4 and List_of_CJK_Unified_Ideographs,_part_4_of_4
    text = mw.ustring.gsub(text, '[^一-鿌]', '')
    len = mw.ustring.len(text)
    return len
end

-- used within other functions but >> no longer necessary <<
-- returns a hidx-style hiragana sort key attached to |hidx=,
-- e.g. |hidx=はつぐん' when given ばつぐん
function export.hidx(f)
    local text = f.args[1]
    local textsub = ''
    local convertedten = ''
    local result = ''
    local len = 1
    local kyreplace = ''
    kyreplace = mw.ustring.gsub(text,'.',ky)
    if kyreplace == '' then
        result = ('|' .. 'hidx' .. '=')
    end
    text = mw.ustring.gsub(text,'.',kh)
    if mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',dakuten) == '' then
        if kyreplace == '' then else result = ('|' .. 'hidx' .. '=') end
        len = mw.ustring.len(text)
        textsub = mw.ustring.sub(text,2,len)
        convertedten = mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',tenconv)
        result = (result .. convertedten .. textsub .. "'")
    else
        if mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',handakuten) == '' then
            if kyreplace == '' then else result = ('|' .. 'hidx' .. '=') end
            len = mw.ustring.len(text)
            textsub = mw.ustring.sub(text,2,len)
            convertedten = mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',tenconv)
            result = (result .. convertedten .. textsub .. "''")
        else
            if kyreplace == '' then
                result = (result .. text)
            end
        end
    end
    return result
end

-- when counting morae, most small hiragana belong to the previous mora,
-- so for purposes of counting them, they can be removed and the characters
-- can be counted to get the number of morae.  The exception is small tsu,
-- so nonmora_to_empty maps all small hiragana except small tsu.
function export.count_morae(text)
    if type(text) == "table" then
        text = text.args[1]
    end
    -- convert kata to hira (hira is untouched)
    text = mw.ustring.gsub(text, '.', kh)
    -- remove all of the small hiragana such as ょ except small tsu
    text = mw.ustring.gsub(text,'.',nonmora_to_empty)
    -- remove zero-width spaces
    text = mw.ustring.gsub(text, '‎', '')
    -- return number of characters, which should be the number of morae
    return mw.ustring.len(text)
end

-- accepts: any mix of kana
-- returns: a hiragana sort key designed for WMF software (hidx of old)
-- this is like hidx above but doesn't return |hidx=sortkey,
-- just the sort key itself, but unlike hidx above, this
-- replaces the long vowel mark with its vowel
function export.jsort(text)
    if type(text) == "table" then
        text = text.args[1]
    end
    local textsub = ''
    local convertedten = ''
    local result = ''
    local len = 1

    -- remove western spaces, hyphens, and periods
    text = mw.ustring.gsub(text, '[ %-%.]', '')

    text = mw.ustring.gsub(text,'.',kh)

    -- if the first character has dakuten, replace it with the corresponding
    -- character without dakuten and add an apostrophe to the end, e.g.
    -- がす > かす'
    if mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',dakuten) == '' then
        len = mw.ustring.len(text)
        textsub = mw.ustring.sub(text,2,len)
        convertedten = mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',tenconv)
        text = (convertedten .. textsub .. "'")
    else
        -- similar thing but with handuken and two apostrophes, e.g. ぱす -> はす''
        if mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',handakuten) == '' then
            len = mw.ustring.len(text)
            textsub = mw.ustring.sub(text,2,len)
            convertedten = mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',tenconv)
            text = (convertedten .. textsub .. "''")
        end
    end
    -- replace the long vowel mark with the vowel that it stands for
    for key,value in pairs(longvowels) do
        text = mw.ustring.gsub(text,key,value)
    end
    return text
end

-- returns 'yes' if the string contains kana (not exactly is kana)
-- returns 'no' otherwise, including if string is empty
function export.is_kana(f)
    local text = f.args[1]
    if mw.ustring.match(text, '[ぁ-ゖ]') then
        return 'yes'
    end
    if mw.ustring.match(text, '[ァ-ヺ]') then
        return 'yes'
    end
    return 'no'
end

-- returns a sort key with |sort= in front, e.g.
-- |sort=はつぐん' if given ばつぐん
function export.sort(f)
    local text = type(text) == 'table' and f.args[1] or f
    local textsub = ''
    local convertedten = ''
    local result = ''
    local len = 1
    local kyreplace = ''
    kyreplace = mw.ustring.gsub(text,'.',ky)
    if kyreplace == '' then
        result = ('|' .. 'sort' .. '=')
    end
    text = mw.ustring.gsub(text,'.',kh)
    if mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',dakuten) == '' then
        if kyreplace == '' then else result = ('|' .. 'sort' .. '=') end
        len = mw.ustring.len(text)
        textsub = mw.ustring.sub(text,2,len)
        convertedten = mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',tenconv)
        result = (result .. convertedten .. textsub .. "'")
    else
        if mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',handakuten) == '' then
            if kyreplace == '' then else result = ('|' .. 'sort' .. '=') end
            len = mw.ustring.len(text)
            textsub = mw.ustring.sub(text,2,len)
            convertedten = mw.ustring.gsub(mw.ustring.sub(text,1,1),'.',tenconv)
            result = (result .. convertedten .. textsub .. "''")
        else
            if kyreplace == '' then
                result = (result .. text)
            end
        end
    end
    return result
end

-- returns the "stem" of a verb or -i adjective, that is the term minus the final character
function export.definal(f)
    return mw.ustring.sub(f.args[1],1,(mw.ustring.len(f.args[1])-1))
end

-- this generates links to categories of the form
-- Category:Japanese terms spelled with (kanji)
-- which was previously done in Template:ja-kanjitab
-- but depended on the editor entering the right kanji
function export.spelled_with_kanji()
    local PAGENAME = mw.title.getCurrentTitle().text
    --PAGENAME = f.args["pagename"]
    local cats = {}
    local c = ''

    -- remove non-kanji characters
    -- technically 々 is not a kanji, but we want a category for it, so leave it in
    PAGENAME = mw.ustring.gsub(PAGENAME, '[^一-鿌]', '')

    local uniquekanji = ""
    for k in mw.ustring.gmatch(PAGENAME,".") do
        if not mw.ustring.find(uniquekanji,k) then uniquekanji = (uniquekanji .. k) end
    end

    for i = 1, mw.ustring.len(uniquekanji) do
        local c = mw.ustring.sub(uniquekanji,i,i)
        table.insert(cats, '[[Category:Japanese terms spelled with ')
        table.insert(cats, c)
        --table.insert(cats, '|')
        --table.insert(cats, sortkey)
        table.insert(cats, ']]')
        --table.insert(cats, "\n")
        --table.insert(cats, '</nowiki>')
    end

    return table.concat(cats, '')
end

-- see also Template:JAruby
-- meant to be called from another module
function export.add_ruby_backend(term, kana)
    local pattern = ""
    -- holds the whole segments of markup enclosed in <ruby>...</ruby>
    local ruby_markup = {}
    local finished_product = {}
    -- range of kana: '[ぁ-ゖァ-ヺ]'
    -- nonkana: [^ぁ-ゖァ-ヺ]
    local kanji_pattern = "[々一-鿌０-９]"
    local nonkanji_pattern = "[^々一-鿌０-９]"

    -- remove periods and hyphens
    kana = mw.ustring.gsub(kana, '[%.%-%^]', '')

    -- in order to make a pattern that will find the ruby,
    -- replace every unbroken string of kanji with a sub-pattern
    pattern = mw.ustring.gsub(term, kanji_pattern .. '+', '(.+)')
    -- get a pattern like
    -- (.+)ばか(.+)ばか(.+)ばかばかばああか(.+) when given 超ばか猿超ばか猿超ばかばかばああか猿
    -- it turns out we need to keep the spaces sometimes
    -- so that kana don't "leak" in ambiguous cases like 捨すてて撤退 where it's not clear if it's
    -- す, てったい or すて, ったい.  only solution now is to put spaces in the "term" param
    -- if they fall between kana

    -- apply that pattern to the kana to collect the rubies
    -- if this fails, try it without spaces
    if mw.ustring.match(kana, pattern) == nil then kana = mw.ustring.gsub(kana, ' ', '') end

    local ruby = { mw.ustring.match(kana, pattern) }
    -- local ruby = {}
    -- for c in mw.ustring.gmatch(kana, pattern) do table.insert(ruby, c) end

    -- find the kanji strings again and combine them with their ruby to make the <ruby> markup
    local kanji_segments = {}
    for c in mw.ustring.gmatch(term, kanji_pattern .. '+') do table.insert(kanji_segments, c) end

    for i = 1, #kanji_segments do
        table.insert(ruby_markup, "<ruby>" .. kanji_segments[i] .. "<rp>&nbsp;(</rp><rt>" .. ruby[i] .. "</rt><rp>) </rp></ruby>")
    end

    -- actually these are non-kanji segments and may include characters like punctuation
    local kana_segments = {}
    for c in mw.ustring.gmatch(term, nonkanji_pattern .. '+') do table.insert(kana_segments, c) end

    -- put it together

    local longer_length = 1
    if #kana_segments > #ruby_markup then longer_length = #kana_segments else longer_length = #ruby_markup end

    -- if the term starts with a segment of non-kanji, add kana before adding ruby
    if mw.ustring.find(term, '^' .. nonkanji_pattern) ~= nil then
        -- add ruby segment then kana segment until we reach the end of whichever is longer
        for i = 1, longer_length do
            if i <= #kana_segments then table.insert(finished_product, kana_segments[i]) end
            if i <= #ruby_markup then table.insert(finished_product, ruby_markup[i]) end
        end
    else
        -- same as above, but add the ruby before the kana
        for i = 1, longer_length do
            if i <= #ruby_markup then table.insert(finished_product, ruby_markup[i]) end
            if i <= #kana_segments then table.insert(finished_product, kana_segments[i]) end
        end
    end

    -- remove spaces
    for i = 1, #finished_product do finished_product[i] = mw.ustring.gsub(finished_product[i], ' ', '') end

    return '<span style="font-size: 1.2em">' .. table.concat(finished_product, '') .. '</span>'
end

-- replaces the code in Template:ja-readings which accepted kanji readings
-- and displayed them in a consistent format
function export.readings(frame)
    -- only do this if this entry is a kanji page and not some user's page
    local PAGENAME = mw.title.getCurrentTitle().text
    if mw.ustring.match(PAGENAME, "[㐀-䶵一-鿌豈-﹏𠀀-𯨟]") then
        local args = frame:getParent().args

        local goon = args["goon"] or ""
        local kanon = args["kanon"] or ""
        local toon = args["toon"] or ""
        local kanyoon = args["kanyoon"] or ""
        local on = args["on"] or ""
        local kun = args["kun"] or ""
        local nanori = args["nanori"] or ""
        local nazuke = args["nazuke"] or ""
        local nadzuke = args["nadzuke"] or ""
        -- my new field for the actual reading of the _kanji_, not the entire word (which may not even
        -- be written with that kanji) which is what "kun" presently has
        local corekun = args["corekun"] or ""

        -- this holds the finished product composed of wikilinks to be displayed
        -- in the Readings section under the Kanji section
        local links = {}

        if goon ~= "" then table.insert(links, "* '''[[呉音|Goon]]''': " .. goon) end
        if kanon ~= "" then table.insert(links, "* '''[[漢音|Kan’on]]''': " .. kanon) end
        if toon ~= "" then table.insert(links, "* '''[[唐音|Tōon]]''': " .. toon) end
        if kanyoon ~= "" then table.insert(links, "* '''[[慣用音|Kan’yōon]]''': " .. kanyoon) end

        if on ~= "" then
            if goon == "" and kanon == "" and toon == "" and kanyoon == "" then
                table.insert(links, "* '''[[on'yomi|On]]''': " .. on)
            else
                table.insert(links, "* '''[[on'yomi|On]]''' (unclassified): " .. on)
            end
        end

        if kun ~= "" then table.insert(links, "* '''[[kun'yomi|Kun]]''': " .. kun) end

        -- three names for the same thing
        if nanori ~= "" then
            table.insert(links, "* '''[[nanori|Nanori]]''': " .. nanori)
        elseif nazuke ~= "" then
            table.insert(links, "* '''[[nanori|Nanori]]''': " .. nazuke)
        elseif nadzuke ~= "" then
            table.insert(links, "* '''[[nanori|Nanori]]''': " .. nadzuke)
        end

        -- add kanji readings categories
        -- range of hiragana: [ぁ-ゖ]

        -- determine if this is joyo kanji (常用) or jinmeiyo kanji (人名用) or neither (表外)
        local joyo_kanji_pattern = ('[' .. joyo_kanji .. ']')
        local jinmeiyo_kanji_pattern = ('[' .. jinmeiyo_kanji .. ']')
        local sortkey = ""
        if mw.ustring.match(PAGENAME, joyo_kanji_pattern) then sortkey = "Common"
        elseif mw.ustring.match(PAGENAME, jinmeiyo_kanji_pattern) then sortkey = "Names"
        else
            sortkey = "Uncommon"
        end

        local all_usable_readings = (goon .. kanon .. toon .. kanyoon .. on .. corekun)
        for r in mw.ustring.gmatch(all_usable_readings, "[{%[]([ぁ-ゖ].-)[}%]]") do
            table.insert(links, "[[Category:Japanese kanji read as " .. r .. "|" .. sortkey .. "]]")
        end

        -- readings should only be in hiragana
        if mw.ustring.match(all_usable_readings, '[ァ-ヺ]') then
            table.insert(links, "[[Category:Japanese kanji needing attention]]")
        end

        return table.concat(links, "\n")
    end
end

-- do the work of Template:ja-kanji
function export.kanji(frame)
    local PAGENAME = mw.title.getCurrentTitle().text
    -- only do this if this entry is a kanji page and not some user's page
    if mw.ustring.match(PAGENAME, "[一-鿌]") then
        local args = frame:getParent().args
        local grade = args["grade"] or ""
        local rs = args["rs"] or ""
        local style = args["style"] or ""
        local shin = args["shin"] or ""
        local kyu = args["kyu"] or ""

        local wikitext = {}
        local categories = {}

        local catsort = (rs ~= "") and rs or PAGENAME

        -- display the kanji itself at the top at 250% size
        table.insert(wikitext, '<div style="margin-top:1.4em"><span style="font-size:250%"><span class="Jpan">' .. PAGENAME .. '</span></span></div>')

        -- display information for the grade

        -- if grade was not specified, determine it now
        if grade == "" then
            local joyo_kanji_pattern = ('[' .. joyo_kanji .. ']')
            local jinmeiyo_kanji_pattern = ('[' .. jinmeiyo_kanji .. ']')
            if mw.ustring.match(PAGENAME, joyo_kanji_pattern) then grade = "c"
            elseif mw.ustring.match(PAGENAME, jinmeiyo_kanji_pattern) then grade = "n"
            else
                grade = "uc"
            end
        end

        table.insert(wikitext, "(''")
        if grade == "1" then table.insert(wikitext, "[[w:Kyōiku kanji|grade 1 “Kyōiku” kanji]]")
        elseif grade == "2" then table.insert(wikitext, "[[w:Kyōiku kanji|grade 2 “Kyōiku” kanji]]")
        elseif grade == "3" then table.insert(wikitext, "[[w:Kyōiku kanji|grade 3 “Kyōiku” kanji]]")
        elseif grade == "4" then table.insert(wikitext, "[[w:Kyōiku kanji|grade 4 “Kyōiku” kanji]]")
        elseif grade == "5" then table.insert(wikitext, "[[w:Kyōiku kanji|grade 5 “Kyōiku” kanji]]")
        elseif grade == "6" then table.insert(wikitext, "[[w:Kyōiku kanji|grade 6 “Kyōiku” kanji]]")
        elseif grade == "7" or grade == "c" then table.insert(wikitext, "[[w:Jōyō kanji|common “Jōyō” kanji]]")
        elseif grade == "8" or grade == "n" then table.insert(wikitext, "[[w:Jinmeiyō kanji|“Jinmeiyō” kanji used for names]]")
        elseif grade == "9" or grade == "uc" then table.insert(wikitext, "[[w:Hyōgaiji|uncommon “Hyōgai” kanji]]")
        elseif grade == "0" or grade == "r" then table.insert(wikitext, "[[w:Radical_(Chinese_character)|Radical]]")
        else
            table.insert(categories, "[[Category:Japanese terms needing attention/kanji grade]]")
        end

        -- if style was indicated, mention that and provide link to corresponding kanji
        -- (link to shinjitai if this is kyujitai, link to kyujitai if this is shinjitai)

        if style == "s" then
            table.insert(wikitext, ",&nbsp;")
            if kyu == "" then
                table.insert(wikitext, "[[shinjitai]] kanji")
            else
                table.insert(wikitext, '[[shinjitai]] kanji, [[kyūjitai]] form <span lang="ja" class="Jpan">[[' .. kyu .. '#Japanese|' .. kyu .. ']]</span>')
            end
        elseif style == "ky" then
            table.insert(wikitext, ",&nbsp;")
            if shin == "" then
                table.insert(wikitext, "[[kyūjitai]] kanji")
            else
                table.insert(wikitext, '[[kyūjitai]] kanji, [[shinjitai]] form <span lang="ja" class="Jpan">[[' .. shin .. '#Japanese|' .. shin .. "]]</span>")
            end
        end
        table.insert(wikitext, "'')")

        -- add categories
        table.insert(categories, "[[Category:Japanese Han characters|" .. catsort .. "]]")
        if grade == "1" then table.insert(categories, "[[Category:Grade 1 kanji|" .. catsort .. "]]")
        elseif grade == "2" then table.insert(categories, "[[Category:Grade 2 kanji|" .. catsort .. "]]")
        elseif grade == "3" then table.insert(categories, "[[Category:Grade 3 kanji|" .. catsort .. "]]")
        elseif grade == "4" then table.insert(categories, "[[Category:Grade 4 kanji|" .. catsort .. "]]")
        elseif grade == "5" then table.insert(categories, "[[Category:Grade 5 kanji|" .. catsort .. "]]")
        elseif grade == "6" then table.insert(categories, "[[Category:Grade 6 kanji|" .. catsort .. "]]")
        elseif grade == "7" or grade == "c" then table.insert(categories, "[[Category:Common kanji|" .. catsort .. "]]")
        elseif grade == "8" or grade == "n" then table.insert(categories, "[[Category:Kanji used for names|" .. catsort .. "]]")
        elseif grade == "9" or grade == "uc" then table.insert(categories, "[[Category:Uncommon kanji|" .. catsort .. "]]")
        elseif grade == "0" or grade == "r" then table.insert(categories, "[[Category:CJKV radicals| ]]")
        end

        -- error category
        if rs == "" then table.insert(categories, "[[Category:Japanese terms needing attention/radical and strokes]]") end

        return table.concat(wikitext, "") .. table.concat(categories, "\n")
    end
end

local grade1_pattern = ('[' .. grade1 .. ']')
local grade2_pattern = ('[' .. grade2 .. ']')
local grade3_pattern = ('[' .. grade3 .. ']')
local grade4_pattern = ('[' .. grade4 .. ']')
local grade5_pattern = ('[' .. grade5 .. ']')
local grade6_pattern = ('[' .. grade6 .. ']')
local secondary_pattern = ('[' .. secondary .. ']')
local jinmeiyo_kanji_pattern = ('[' .. jinmeiyo_kanji .. ']')
local hyogaiji_pattern = ('[^' .. joyo_kanji .. jinmeiyo_kanji .. ']')

function export.kanji_grade(kanji)
    if type(kanji) == "table" then
        kanji = kanji.args[1]
    end

    if mw.ustring.match(kanji, hyogaiji_pattern) then return 9
    elseif mw.ustring.match(kanji, jinmeiyo_kanji_pattern) then return 8
    elseif mw.ustring.match(kanji, secondary_pattern) then return 7
    elseif mw.ustring.match(kanji, grade6_pattern) then return 6
    elseif mw.ustring.match(kanji, grade5_pattern) then return 5
    elseif mw.ustring.match(kanji, grade4_pattern) then return 4
    elseif mw.ustring.match(kanji, grade3_pattern) then return 3
    elseif mw.ustring.match(kanji, grade2_pattern) then return 2
    elseif mw.ustring.match(kanji, grade1_pattern) then return 1
    end

    return false
end

function export.new(frame)
    local args = frame:getParent().args
    local result = "==Japanese=="
    if args["wp"] then
        result = result .. "\n{{wikipedia|lang=ja}}"
    end

    pagename = mw.title.getCurrentTitle().text
    text = args[1] ~= "" and args[1] or pagename
    text = mw.ustring.gsub(text, "%-", "|")

    local function make_tab(original, yomi)
        output_text = ""
        original = mw.ustring.gsub(original, " ", "|")
        if mw.ustring.match(original, "<") then
            for word in mw.ustring.gmatch(original, "<([^>]+)>") do
                output_text = output_text .. "|" .. word
            end
            yomi = "k"
        else
            output_text = mw.ustring.gsub(original, ">([1-9])", "|k%1=")
            output_text = mw.ustring.match(output_text, "|") and "|" .. output_text or false
        end
        yomi = yomi or "o"
        return "\n{{ja-kanjitab" .. (output_text or "") .. "|yomi=" .. yomi .. "}}", yomi
    end
    if mw.ustring.match(pagename, "[一-龯㐀-䶵]") then
        to_add, yomi = make_tab(text, args["yomi"])
        result = result .. to_add
    end

    local no_dash = {}
    for syllable in mw.text.gsplit(text, "|", true) do
        syllable = mw.ustring.gsub(syllable, (mw.ustring.match(syllable, "<") and "[<>]" or "^.+>[0-9]"), "")
        table.insert(no_dash, syllable)
    end

    text = table.concat(no_dash)

    local function other(class, title, args)
        local code = ""
        if args[class] then
            code = code .. "\n\n===" .. title .. "===\n* {{l/ja|" .. args[class] .. "}}"

            if args[class .. "2"] then
                code = code .. "\n* {{l/ja|" .. args[class .. "2"] .. "}}"

                if args[class .. "3"] then
                    code = code .. "\n* {{l/ja|" .. args[class .. "3"] .. "}}"

                    if args[class .. "4"] then
                        code = code .. "\n* {{l/ja|" .. args[class .. "4"] .. "}}"
                    end
                end
            end
        end
        return code
    end

    result = result .. other("alt", "Alternative forms", args)

    sortkey = export.script(text) == "Kana" and export.sort(text) or false
    if sortkey and sortkey == "|sort=" .. text then
        sortkey = false
    end

    if args["e"] or args["ee"] or args["we1"] then
        result = result .. "\n\n===Etymology===\n"
        if args["we1"] then
            result = result .. "{{wasei eigo|" .. args["we1"] .. (args["we2"] and "|" .. args["we2"] or "") .. (sortkey or "") .. "}}"
        else
            result = result .. (args["ee"] or
                    ("From {{etyl|" .. (args["el"] or "en") .. "|ja" .. (sortkey or "") .. "}} {{m|" ..
                            (args["el"] or "en") .. "|" .. args["e"] .. "}}."))
        end
    end

    result = result .. "\n\n===Pronunciation===\n{{ja-pron" .. (args[1] ~= "" and "|" .. text or "") .. (yomi and "|y=" .. yomi or "") .. "}}"

    local pos = args[2] ~= "" and args[2] or "n"
    local pos_table = {
        ["n"] = { "Noun", "noun", true },
        ["s"] = { "Noun", "noun", true, "Verb", "verb-suru" },
        ["noun"] = { "Noun", "noun", true },
        ["suru"] = { "Noun", "noun", true, "Verb", "verb-suru" },
        ["an"] = { "Adjective", "adj", true, "Noun", "noun" },
        ["anoun"] = { "Adjective", "adj", true, "Noun", "noun" },
        ["v"] = { "Verb", "verb", true },
        ["verb"] = { "Verb", "verb", true },
        ["vform"] = { "Verb", "verb form", true },
        ["verb form"] = { "Verb", "verb form", true },
        ["a"] = { "Adjective", "adj", true },
        ["adj"] = { "Adjective", "adj", true },
        ["adjective"] = { "Adjective", "adj", true },
        ["adv"] = { "Adverb", "adverb", false },
        ["adverb"] = { "Adverb", "adverb", false },
        ["pn"] = { "Proper noun", "proper", false },
        ["propn"] = { "Proper noun", "proper", false },
        ["proper noun"] = { "Proper noun", "proper", false },
        ["ph"] = { "Phrase", "phrase", true },
        ["phrase"] = { "Phrase", "phrase", true },
        ["intj"] = { "Interjection", "interjection", false },
        ["conj"] = { "Conjunction", "conjunction", false },
        ["part"] = { "Particle", "particle", false },
        ["prep"] = { "Preposition", "preposition", false },
    }

    result = result .. "\n\n===" .. pos_table[pos][1] .. "===\n{{ja-" .. (not pos_table[pos][3] and "pos|" or "") .. pos_table[pos][2] ..
            (args[1] ~= "" and "|" .. text or "")

    if pos_table[pos][1] == "Adjective" then
        result = result .. "|infl=" .. (args["infl"] and args["infl"] or "na")
    end

    result = result .. (args["type"] and "|type=" .. args["type"] or "") .. (args["tr"] and "|tr=" .. args["tr"] or "") .. "}}"
    result = result .. "\n\n# " .. args[3]

    result = result .. other("syn", "=Synonyms=", args)
    result = result .. other("ant", "=Antonyms=", args)
    result = result .. other("der", "=Derived terms=", args)
    result = result .. other("also", "=See also=", args)

    if pos_table[pos][1] == "Adjective" then
        result = result .. "\n\n====Inflection====\n"
        if args["infl"] == "i" or args["infl"] == "い" then
            result = result .. "{{ja-i" .. (args[1] ~= "" and "|" .. mw.ustring.sub(text, 1, -2) or "") .. "}}"
        else
            result = result .. "{{ja-na" .. (args[1] ~= "" and "|" .. args[1] or "") .. "}}"
        end
    end

    if pos_table[pos][2] == "verb" then
        result = result .. "\n\n====Conjugation====\n{{ja-"
        penul = mw.ustring.sub(mw.ustring.gsub(mw.ustring.gsub(mw.ustring.sub(text, -2, -2), ".", hk), ".", kr), -1, -1)
        cons = mw.ustring.sub(mw.ustring.gsub(mw.ustring.gsub(mw.ustring.sub(text, -1, -1), ".", hk), ".", kr), 1, 1)
        if cons == "u" then
            cons = ""
        elseif cons == "t" then
            cons = "ts"
        end
        if final == "る" and (penul == "i" or penul == "e") and args["type"] == 2 then
            result = result .. "ichi"
        else
            result = result .. "go-" .. cons .. "u"
        end

        result = result .. (args[1] ~= "" and "|" .. mw.ustring.sub(text, 1, -2) or "") .. "}}"
    end

    if pos_table[pos][4] and args[4] ~= "" then
        result = result .. "\n\n===" .. pos_table[pos][4] .. "===\n{{ja-" .. pos_table[pos][5] .. (args[1] ~= "" and "|" .. text or "") ..
                (args["type"] and "|type=" .. args["type"] or "") .. (args["tr"] and "|tr=" .. args["tr"] or "") .. "}}\n\n#" .. args[4]

        if pos_table[pos][4] == "Verb" then
            result = result .. "\n\n====Conjugation====\n{{ja-suru" .. (args[1] ~= "" and "|" .. text or "") .. "}}"
        end
    end

    if args["k"] then
        result = result .."\n\n----\n\n==Korean==\n{{ko-hanjatab}}\n\n===" .. pos_table[args["kp"] or "n"][1] ..
                "===\n{{ko-" .. pos_table[args["kp"] or "n"][2] .. "|hj" .. "|hangeul=" .. args["k"] .. "}}" ..
                "\n\n# {{hanja form of|" .. args["k"] .. "|" .. (args["kd"] or args[3]) .. "}}"
    end

    return result
end

return export
