package main.origo;

import com.google.common.collect.Sets;
import main.origo.core.Node;
import main.origo.core.State;
import main.origo.core.actions.Component;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.themes.bootstrap.BootstrapTheme;
import models.origo.authentication.BasicAuthorization;
import models.origo.authentication.BasicRole;
import models.origo.authentication.BasicUser;
import models.origo.core.*;
import models.origo.core.navigation.BasicNavigation;
import models.origo.core.navigation.ExternalLinkNavigation;
import models.origo.core.navigation.GroupHolderNavigation;
import models.origo.core.navigation.InternalPageIdNavigation;

import java.util.UUID;

public class SampleDataCreator {

    public static void create() {
        if (Settings.load().getValue(CoreSettingsHelper.Keys.BASE_URL) == null) {
            createSettings();
            createPage1();
            createPage2();
            createPage3();
            createPage4();
            createPage5();
            createPage6();
            createPage7();
            createPage8();
            createNavigation();
            createUsersAndRoles();
        }
    }

    private static void createSettings() {
        Settings settings = Settings.load();
        settings.setValueIfMissing(CoreSettingsHelper.Keys.BASE_URL, "/");
        settings.setValueIfMissing(CoreSettingsHelper.Keys.START_PAGE, "2c36c55dd-956e-4b78-18c4-eef7e56aa17"); // Page 1
        settings.setValueIfMissing(CoreSettingsHelper.Keys.PAGE_NOT_FOUND_PAGE, "c9615819-0556-4e70-b6a9-a66c5b8d4c1a"); // Page 2
        settings.setValueIfMissing(CoreSettingsHelper.Keys.INTERNAL_SERVER_ERROR_PAGE, "1cf699a7-a0c4-4be0-855f-466042a36a8d"); // Page 3
        settings.setValueIfMissing(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE, "f4501c31-690f-46f4-853d-167165a4fc03"); // Page 6
        settings.setValueIfMissing(CoreSettingsHelper.Keys.USER_TYPE, BasicUser.TYPE);
        settings.setValue(CoreSettingsHelper.Keys.THEME, BootstrapTheme.ID); // Override theme variant
        settings.setValue(CoreSettingsHelper.Keys.THEME_VARIANT, "bootstrap-main_only"); // Override theme variant

        settings.save();
    }

    private static void createPage1() {

        Release release1 = getOrCreateRelease("First Release");
        Release release2 = getOrCreateRelease("Second Release");

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, "2c36c55dd-956e-4b78-18c4-eef7e56aa17", 1),
                "Start Page",
                createTextBlock("Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. " +
                        "\"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin."),
                createTextBlock("Version 1: Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. " +
                        "\"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                        "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. " +
                        "Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                        "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! " +
                        "Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                        "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! " +
                        "\"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                        "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? " +
                        "Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!"));

        createPage(
                createRootNode(BasicPage.TYPE, "2c36c55dd-956e-4b78-18c4-eef7e56aa17", 2, release1),
                "Start Page",
                createTextBlock("Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. " +
                        "\"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin."),
                createTextBlock("Version 2: Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. " +
                        "\"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                        "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. " +
                        "Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                        "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! " +
                        "Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                        "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! " +
                        "\"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                        "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? " +
                        "Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!"));

        createPage(
                createRootNode(BasicPage.TYPE, "2c36c55dd-956e-4b78-18c4-eef7e56aa17", 3, release2),
                "Start Page",
                createTextBlock("Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. " +
                        "\"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin."),
                createTextBlock("Version 3: Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. " +
                        "\"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                        "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. " +
                        "Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                        "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! " +
                        "Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                        "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! \"Boo flooble da?\" zung zoweebizzle. " +
                        "Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                        "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? Quabble dee blab flibble? " +
                        "Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!"));

        // /start -> page 1
        createAlias("start", page.nodeId());

    }

    private static void createPage2() {

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, UUID.randomUUID().toString(), 1),
                "Page Not Found",
                "bootstrap-main_only",
                createTextBlock("Yip wiggle the Antichrist... bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! " +
                        "Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>\n" +
                        "Zongle yip flob boo dong flub zip dubba? Yap flabbing blobdabba! Ho hum Cartman quibbleflibble! Tingle dee dabba dingle? Twaddle quabble wiggle doo yada rizzle dongle hum dabba. Tangle zangle bam flip boo fling? Da cake abracadabra?" +
                        "\"Roo whack nip?\" wooble wackoongle.<br/>\n"),
                createTextBlock("Zap sloppy razzleflap! Nip doo bizzlewibble dee duh flobble, zip bleebity duh blab da. \"Doo flong dee?\" nizzle Kyle. Goblin weeble ha bling da shnizzle? Dazzledee-blobbing-doof! Do-da woogle blung! " +
                        "Ho dobba Kyle...blingity blongity flubblung. Da bam Chaka Khan zonkwaggle!<br/>\n" +
                        "Bleebity janglewoogle. Nip flupping flunging blungity bam boo zap crongely wogglenizzle. Da cringle flooblecake. Duh boo cringleflong loo dee zonk, zip flobbity zip fraggle bam. Bam shnoz yip shnozzle raz-ma tangblip, " +
                        "dee blong dizzle roo flibbity bingyada wubble. Twaddle bleep linghizzle, \"tingle da ha blung,\" nip Rev. Lovejoy shnaz razzle boo blung-wubble...tang dee boo! Whack boo waggle ho zangle-dobba!! Blee boo flobble-nizzle.<br/>\n" +
                        "Blong zip goblin loo zangle ling duh dabba? Flanging razzzingle. \"Loo dongle duh?\" blup wobbleling. Ha flobbity flongity zingity loo dong nip crangely onglezingle. Hum blopping shnuzzle goblin shnuzzle duh dong. " +
                        "Duh wubble yip shnizzle zungity blopmeep, duh oodle shrubbery zip shizzely blupdang flub. Hum zip The Honorable Mayor Marion Barry wiggleblap! Waggle da blip hum meep zonk duh blee?<br/>\n" +
                        "Shnuzzle yada ting loo shnazzle zung shnuzzle zap bloo. Boo blabbity flobbing shruberific ho blip da dongely shnuzzlequibble. Shnozzy bla tizzle! Bizzle zowee doo quabble boo twiddle? Blob loo jongely blingwoogle. " +
                        "Ha plop shnoz hizzyyada hum yip roo \"bluppity wooble-twiddle\". Rizzle blee? Flobble bing zip blung duh ding?<br/>\n" +
                        "Zungle bam blob flibble? Hum hum doo Principal Skinner blameep. Tang! Bling bling tizzleabracadabra, \"zing yip nip blap,\" yap zunk shrubbery oodle boo woogle-dubba...flop nip zap! Da blo dobba dongleflop doo boo zap " +
                        "\"zappity zoom-zing\". Bam loo weeble zung flanging shizzely bleeb blangcringle? Blip zong zoom yap zoom ding hizzy ho flup. Crongely bleeb razzle!\n"));

        // ### Alias ###
        // /page-not-found -> page 2
        createAlias("page-not-found", page.nodeId());

    }

    private static void createPage3() {

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, UUID.randomUUID().toString(), 1),
                "Internal Server Error",
                "bootstrap-main_only",
                createTextBlock("Razzle dee crongely zoomzunk. Hizzy cake yap zingle duh dobba? Zungity wubbledongle. Oodle yip weeble-flip.<br/>\n" +
                        "Flab flab blupping doo weebleflub??? Flobble da woggle-shnozzle. Bleep flub dee wibble hum flong? Doof nip zongity blipzonk. Tangle gobble flungity zip hizzleflong???<br/>\n"),
                createTextBlock("Ha gobble fling? Boo nip Trotsky floodazzle! Zangboo-bleebity-dazzle! Zangle dee tingle yip bing zongle duh plop? Flap zong? Tizzle fling? Loo shnizzle yip blong bleebing twaddlewhack, zap bananarama wacko ha blingity abracadabraflee tang. Yap bam tangle whack ting a blingity blo tongleflap?<br/>\n" +
                        "Loo zang Stan...zingity flonging blupzong. \"Zip bleep hum?\" noodle tingweeble. Abracadabra zangle dingleplop, \"flup ho yip fling,\" bam crangle woogle hizzle boo flop-shnuzzle...zongle zip ho! Hizzy! Meep rizzle crungely zap tanglebizzle??? Blo bla dang loo slap bloo dang loo flip. Cake shnuzzle zangle zap wacko dubba bleeb roo bang. Loo zungle blopflobble.<br/>\n" +
                        "Zoom ho ongle hum flibble bizzle doo dongle? Yap oodle Mr. Hat...flangity jingely izzlewaggle. Shruberific caketangle. Zing zap blap bleeb. \"Hum zowee ho?\" izzle dilznoofusdizzle. Noodle wobble bing boo crongle blob dingle doo crongle. Blob doo zowee-twaddle. Jackson roo tang roo blip blingity wugglezang.\n"));

        // /error -> page 3
        createAlias("error", page.nodeId());

    }

    private static void createPage4() {

        Release release1 = getOrCreateRelease("First Release");

        Text text_1 = createText("Boo crangle Miss Beasley... zonkity flobbing zinglemeep. Rizzle wheezer wibblequibble, \"kanoodle zap yip zang,\" zap Jackson wheezer flup loo flong-bananarama...blang loo da! Bam yip zip Clinton yadazangle.<br/> " +
                "Noodle crungle? Bang zing hum fraggle nip wubble? Zip roo twaddle shnuzzle flobbity flopping whack fragglemeep?");

        Text text_2 = createText("Yip wiggle the Antichrist... bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>" +
                "Zongle yip flob boo dong flub zip dubba? Yap flabbing blobdabba! Ho hum Cartman quibbleflibble! Tingle dee dabba dingle? Twaddle quabble wiggle doo yada rizzle dongle hum dabba. Tangle zangle bam flip boo fling? Da cake abracadabra? \"Roo whack nip?\" wooble wackoongle.<br/>" +
                "Fluppity zonk bleeb! Razzle ha crongle! Bang da crangle! Tizzle boo? Blab dobba wobble dee dubba bing shnozzle doo zoom. Dabba bam flib-flobble. Flub dee dang bananarama? Jangle shnoz flangity loo wobblezong???<br/>" +
                "Zunk flup bloptizzle, \"twaddle nip yap woogle,\" yip Chef yada bleeb yip wubble-floo...blab roo duh! Flanders yip Stan doo flipping izzle-tingle. Dee ho meep razz zang a dingely gobble razzledoof? Zingle hum zowee nip zung-zangle!! Marge hum OJ yip bleepity zowee-zangle. Dee duh shnozflung hum yip flip, nip flupping ho floo yap. Doo ho Clinton crangleflab! \"Doo bananarama yip?\" ling You.<br/>" +
                "Principal Skinner bam ling ha zongle blabbing razzcrongle. Doo razz bing razzleflooble ha boo hum \"flappity ling-ting\". Flibdee-blangity-jingle! Blob zungle? Shnazzle bling da waggle nip dobba? Tizzle dizzle bingblo, \"flap yap boo zing\", yip whack blab flibble duh hizzy-flop...oodle hum ho! Yap ingle zangle? Flubbing bleepwhack.");

        Text text_3 = createText("Dubba loo bling! Hizzy dee flip fraggle. Flappity whackboo.<br/>" +
                "\"Nip boo da?\" dang TIMMY. Slapyip-tang a-dingle! Dilznoofus dee bloppity bananaramawoggle.");

        // Page 4 -> Block
        Block block_1_1 = createTextBlock(text_1); // Page 4 Version 1, Block 1
        Block block_1_2 = createTextBlock(text_2); // Page 4 Version 1, Block 2
        Block block_1_3 = createTextBlock(text_3); // Page 4 Version 1, Block 3

        BasicPage page1 =
                createPage(
                        createRootNode(BasicPage.TYPE, "aa1755dd-18c4-4b78-956e-eef7e562c36c", 1),
                        "Fourth Page",
                        "bootstrap-main_and_left_columns",
                        block_1_1,
                        block_1_2,
                        block_1_3);

        createMeta(page1, block_1_1, "left", 100);  // Page 4 Version 1 Block 1 Meta
        createMeta(page1, block_1_2, "main", 50); // Page 4 Version 1 Block 2 Meta
        createMeta(page1, block_1_3, "main", 100); // Page 4 Version 1 Block 3 Meta

        Block block_2_1 = createTextBlock(text_1); // Page 4 Version 2, Block 1
        Block block_2_2 = createTextBlock(text_2); // Page 4 Version 3, Block 2

        BasicPage page2 =
                createPage(
                        createRootNode(BasicPage.TYPE, "aa1755dd-18c4-4b78-956e-eef7e562c36c", 2, release1),
                        "Fourth Page",
                        "bootstrap-main_and_left_columns",
                        block_2_1,
                        block_2_2);

        createMeta(page2, block_2_1, "left", 50); // Page 4 Version 2 Block 1 Meta
        createMeta(page2, block_2_2, "main", 100); // Page 4 Version 2 Block 2 Meta

        // /fourth -> page 4
        createAlias("fourth", page1.nodeId());

    }

    private static void createPage5() {

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, "699eb321-7545-4b27-8a7f-94a4442d2046", 1),
                "Protected Page",
                createTextBlock("Goblin abracadabra dobbawacko, \"whack roo dee shnazzle,\" boo shnozzle wuggle fling nip razzle-wacko...crungle hum dee! Flung bam dizzle loo bleeb shnaz nip ingle? Zonkha-izzle-boo!"),
                createTextBlock("Blung wooble duh tizzle bam wiggle? Ho nip doo Principal Skinner shnuzzlecringle. Flung yap bling boo crongle-blob!! Zap zap da Chaka Khan zonkshnazzle. Dee boo Smithers flibzung! " +
                        "Blob ha blang zip flap twaddle dee blob? Flib da zongity blooflee. Dingleloo-zapping-bling!\n\n\"Bam ingle da?\" slop flibfloo. Da yip yap Kenny wubbletang. Yap blab goblinwiggle. " +
                        "Ha crangle hum wobble cringely wogglewibble, loo flang razz roo slappy shnizzlecrangle zoom. Jingle flee cringlewacko, \"blung ha duh bang,\" roo blee tongle tingle loo razz-zang...hizzy zap duh! " +
                        "\"Boo dobba dee?\" quibble razzwacko. Flob hum hizzle duh wubble bizzle yap blee? Zip ha Luke zonkshnazzle!"));

        // /protected -> page 5
        createAlias("protected", page.nodeId());

    }

    private static void createPage6() {

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, "f4501c31-690f-46f4-853d-167165a4fc03", 1),
                "Unauthorized Access",
                createTextBlock("Goblin abracadabra dobbawacko, \"whack roo dee shnazzle,\" boo shnozzle wuggle fling nip razzle-wacko...crungle hum dee! Flung bam dizzle loo bleeb shnaz nip ingle? Zonkha-izzle-boo!"),
                createTextBlock("Blung wooble duh tizzle bam wiggle? Ho nip doo Principal Skinner shnuzzlecringle. Flung yap bling boo crongle-blob!! Zap zap da Chaka Khan zonkshnazzle. Dee boo Smithers flibzung! " +
                        "Blob ha blang zip flap twaddle dee blob? Flib da zongity blooflee. Dingleloo-zapping-bling!\n\n\"Bam ingle da?\" slop flibfloo. Da yip yap Kenny wubbletang. Yap blab goblinwiggle. " +
                        "Ha crangle hum wobble cringely wogglewibble, loo flang razz roo slappy shnizzlecrangle zoom. Jingle flee cringlewacko, \"blung ha duh bang,\" roo blee tongle tingle loo razz-zang...hizzy zap duh! " +
                        "\"Boo dobba dee?\" quibble razzwacko. Flob hum hizzle duh wubble bizzle yap blee? Zip ha Luke zonkshnazzle!"));

        // /error -> page 6
        createAlias("unauthorized", page.nodeId());

    }

    private static void createPage7() {

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, "807f2ece-c143-4f32-88db-1e1dfcd3e2d9", 1),
                "Component Test Page",
                createTextBlock("This page has a component in the body of the page."),
                createComponentBlock("d7728926-3e27-44a6-be71-8c2d1c95c2ae"));

        // /component -> page 7 (component)
        createAlias("component", page.nodeId());

    }

    private static void createPage8() {

        BasicPage page = createPage(
                createRootNode(BasicPage.TYPE, UUID.randomUUID().toString(), 1),
                "Preview Ticket",
                createTextBlock("Preview Tickets are used to view how the content will be displayed at a certain date and time."),
                createComponentBlock("c26ba92a-9cec-43ae-b63f-6e5c4f6b1eb9"));

        // /preview -> page 7 (component)
        createAlias("preview", page.nodeId());

        // /preview -> page 7 (component)
        createAlias("preview/submit", page.nodeId());

        // /preview -> page 7 (component)
        createAlias("preview/remove", page.nodeId());

    }

    private static void createNavigation() {

        // Start Page
        BasicNavigation startNavigation = new BasicNavigation();
        startNavigation.type = InternalPageIdNavigation.TYPE;
        startNavigation.section = "front";
        startNavigation.referenceId = "3f2d9e2e-12dc-4917-9a58-40d325e9784e";
        startNavigation.weight = 1;
        startNavigation.create();
        InternalPageIdNavigation startPageNavigation = new InternalPageIdNavigation();
        startPageNavigation.identifier = startNavigation.getReferenceId();
        startPageNavigation.pageId = "2c36c55dd-956e-4b78-18c4-eef7e56aa17";
        startPageNavigation.create();

        // Fourth
        BasicNavigation fourthNavigation = new BasicNavigation();
        fourthNavigation.type = InternalPageIdNavigation.TYPE;
        fourthNavigation.section = "front";
        fourthNavigation.referenceId = "a8129a97-70fa-40b7-93e4-2a1caf181a0d";
        fourthNavigation.weight = 3;
        fourthNavigation.create();
        InternalPageIdNavigation fourthPageNavigation = new InternalPageIdNavigation();
        fourthPageNavigation.identifier = fourthNavigation.getReferenceId();
        fourthPageNavigation.pageId = "aa1755dd-18c4-4b78-956e-eef7e562c36c";
        fourthPageNavigation.create();

        // Group
        BasicNavigation groupNavigation = new BasicNavigation();
        groupNavigation.type = GroupHolderNavigation.TYPE;
        groupNavigation.section = "front";
        groupNavigation.referenceId = "085ffde4-b8d5-4fd6-82a7-5c6787931f1b";
        groupNavigation.weight = 5;
        groupNavigation.create();
        GroupHolderNavigation externalNavigationHolder = new GroupHolderNavigation();
        externalNavigationHolder.identifier = groupNavigation.getReferenceId();
        externalNavigationHolder.title = "Other Pages";
        externalNavigationHolder.create();

        // Fifth
        BasicNavigation fifthNavigation = new BasicNavigation();
        fifthNavigation.type = InternalPageIdNavigation.TYPE;
        fifthNavigation.section = "front";
        fifthNavigation.referenceId = "9c0cbb5a-e90a-43bf-a647-119c27e30e9d";
        fifthNavigation.weight = 5;
        fifthNavigation.parent = groupNavigation;
        fifthNavigation.create();
        InternalPageIdNavigation fiftPageNavigation = new InternalPageIdNavigation();
        fiftPageNavigation.identifier = fifthNavigation.getReferenceId();
        fiftPageNavigation.pageId = "699eb321-7545-4b27-8a7f-94a4442d2046";
        fiftPageNavigation.create();

        // Seventh (Component)
        BasicNavigation seventhNavigation = new BasicNavigation();
        seventhNavigation.type = InternalPageIdNavigation.TYPE;
        seventhNavigation.section = "front";
        seventhNavigation.referenceId = "436e626e-fd01-4fce-93b7-23c49c33a913";
        seventhNavigation.weight = 2;
        seventhNavigation.parent = groupNavigation;
        seventhNavigation.create();
        InternalPageIdNavigation seventhPageNavigation = new InternalPageIdNavigation();
        seventhPageNavigation.identifier = seventhNavigation.getReferenceId();
        seventhPageNavigation.pageId = "807f2ece-c143-4f32-88db-1e1dfcd3e2d9";
        seventhPageNavigation.create();

        // External
        BasicNavigation externalGroupNavigation = new BasicNavigation();
        externalGroupNavigation.type = GroupHolderNavigation.TYPE;
        externalGroupNavigation.section = "front";
        externalGroupNavigation.referenceId = "6dd82bcb-3f42-4f5d-8c13-4e2ed1d4ef21";
        externalGroupNavigation.weight = 10;
        externalGroupNavigation.create();
        GroupHolderNavigation externalGroupHolderNavigation = new GroupHolderNavigation();
        externalGroupHolderNavigation.identifier = externalGroupNavigation.getReferenceId();
        externalGroupHolderNavigation.title = "External";
        externalGroupHolderNavigation.create();

        // External - Google
        BasicNavigation googleNavigation = new BasicNavigation();
        googleNavigation.type = ExternalLinkNavigation.TYPE;
        googleNavigation.section = "front";
        googleNavigation.referenceId = "58e15bfa-da4f-4f6b-a15a-51ab6c82e670";
        googleNavigation.parent = externalGroupNavigation;
        googleNavigation.weight = 2;
        googleNavigation.create();
        ExternalLinkNavigation googleLink = new ExternalLinkNavigation();
        googleLink.identifier = googleNavigation.getReferenceId();
        googleLink.title = "Google";
        googleLink.link = "http://www.google.com";
        googleLink.create();

        // External - Yahoo
        BasicNavigation yahooNavigation = new BasicNavigation();
        yahooNavigation.type = ExternalLinkNavigation.TYPE;
        yahooNavigation.section = "front";
        yahooNavigation.referenceId = "c6f03b11-dbb6-4aec-a325-525e61370d8d";
        yahooNavigation.parent = externalGroupNavigation;
        yahooNavigation.weight = 3;
        yahooNavigation.create();
        ExternalLinkNavigation yahooLink = new ExternalLinkNavigation();
        yahooLink.identifier = yahooNavigation.getReferenceId();
        yahooLink.title = "Yahoo";
        yahooLink.link = "http://www.yahoo.com";
        yahooLink.create();

    }

    private static void createUsersAndRoles() {
        BasicRole simpleRole = new BasicRole();
        simpleRole.name = "Normal";
        simpleRole.create();

        BasicUser basicUser = new BasicUser();
        basicUser.roles.add(simpleRole);
        basicUser.email = "user@email.com";
        basicUser.password = "password";
        basicUser.create();

        BasicRole adminRole = new BasicRole();
        adminRole.name = "Admin";
        adminRole.create();

        BasicUser adminUser = new BasicUser();
        adminUser.roles.add(simpleRole);
        adminUser.roles.add(adminRole);
        adminUser.email = "admin@email.com";
        adminUser.password = "password";
        adminUser.create();

        BasicAuthorization adminAuthorization = new BasicAuthorization();
        adminAuthorization.path = "/admin";
        adminAuthorization.roles = Sets.newHashSet("Admin");
        adminAuthorization.create();

        BasicAuthorization previewAuthorization = new BasicAuthorization();
        previewAuthorization.path = "/preview";
        previewAuthorization.roles = Sets.newHashSet("Admin");
        previewAuthorization.create();

        BasicAuthorization testAuthorization = new BasicAuthorization();
        testAuthorization.path = "699eb321-7545-4b27-8a7f-94a4442d2046"; // Page 5
        testAuthorization.roles = Sets.newHashSet("!Admin", "Normal");
        testAuthorization.create();

    }

    private static RootNode createRootNode(String type, String nodeId, int version) {
        RootNode node = new RootNode(nodeId, version);
        node.nodeType(type);
        node.create();
        return node;
    }

    private static RootNode createRootNode(String type, String nodeId, int version, Release release) {
        RootNode node = new RootNode(nodeId, version, type);
        node.release(release);
        node.create();
        return node;
    }

    private static Release getOrCreateRelease(String name) {
        Release release = Release.findWithName(name);
        if (release == null) {
            release = new Release();
            release.name = name;
            release.state = State.DRAFT;
            release.create();
        }
        return release;
    }

    private static BasicPage createPage(RootNode node, String title, Block... blocks) {
        BasicPage page = new BasicPage();
        page.rootNode = node;
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = title;
        for (Block block : blocks) {
            page.blocks.add(block.identifier);
        }
        return page.create();
    }

    private static BasicPage createPage(RootNode node, String title, String themeVariant, Block... blocks) {
        BasicPage page = createPage(node, title, blocks);
        page.themeVariant = themeVariant;
        return page;
    }

    private static Text createText(String body) {
        Text text = new Text();
        text.identifier = UUID.randomUUID().toString();
        text.value = body;
        text.create();
        return text;
    }

    private static Block createTextBlock(String body) {
        return createTextBlock(createText(body));
    }

    private static Block createTextBlock(Text text) {
        Block block = new Block();
        block.identifier = UUID.randomUUID().toString();
        block.referenceId = text.identifier;
        block.type = Text.TYPE;

        return block.create();
    }

    private static Block createComponentBlock(String identifier) {

        Block block = new Block();
        block.identifier = UUID.randomUUID().toString();
        block.referenceId = identifier;
        block.type = Component.TYPE;

        return block.create();
    }

    private static Meta createMeta(Node node, Block block, String region, int weight) {
        Meta meta = new Meta();
        meta.nodeId = node.nodeId();
        meta.version = node.version();
        meta.weight = weight;
        meta.region = region;
        meta.referenceId = block.identifier;
        return meta.create();
    }

    private static Alias createAlias(String title, String nodeId) {
        Alias start = new Alias(title, nodeId);
        return start.create();
    }

}
