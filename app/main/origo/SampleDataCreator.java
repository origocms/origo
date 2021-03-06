package main.origo;

import com.google.common.collect.Sets;
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
import models.origo.structuredcontent.Segment;
import models.origo.structuredcontent.StructuredPage;
import org.joda.time.DateTime;

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

        Content lead = new Content();
        lead.identifier = "2dd34035-7940-42c5-aadc-9710153fc7c9";
        lead.value = "Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin.";
        lead.create();

        Content bodyVersion1 = new Content();
        bodyVersion1.identifier = "8659c030-6fd5-4d8e-8304-9671d4e5857f";
        bodyVersion1.value = "Version 1: Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! \"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!";
        bodyVersion1.create();

        RootNode nodeVersion1 = new RootNode("2c36c55dd-956e-4b78-18c4-eef7e56aa17", 1);
        nodeVersion1.nodeType(BasicPage.TYPE);
        nodeVersion1.create();

        BasicPage pageVersion1 = new BasicPage();
        pageVersion1.nodeId = nodeVersion1.nodeId();
        pageVersion1.version = nodeVersion1.version();
        pageVersion1.title = "Start Page";
        pageVersion1.leadReferenceId = lead.identifier;
        pageVersion1.bodyReferenceId = bodyVersion1.identifier;
        pageVersion1.create();

        Content bodyVersion2 = new Content();
        bodyVersion2.identifier = "8659c030-6fd5-4d8e-8304-9671d4e5857f";
        bodyVersion2.value = "Version 2: Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! \"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!";
        bodyVersion2.create();

        RootNode nodeVersion2 = new RootNode("2c36c55dd-956e-4b78-18c4-eef7e56aa17", 2);
        nodeVersion2.nodeType(BasicPage.TYPE);
        nodeVersion2.published(DateTime.now().plusMonths(1).toDate());
        nodeVersion2.create();

        BasicPage pageVersion2 = new BasicPage();
        pageVersion2.nodeId = nodeVersion2.nodeId();
        pageVersion2.version = nodeVersion2.version();
        pageVersion2.title = "Start Page";
        pageVersion2.leadReferenceId = lead.identifier;
        pageVersion2.bodyReferenceId = bodyVersion2.identifier;
        pageVersion2.create();

        Content bodyVersion3 = new Content();
        bodyVersion3.identifier = "8659c030-6fd5-4d8e-8304-9671d4e5857f";
        bodyVersion3.value = "Version 3: Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! \"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!";
        bodyVersion3.create();

        RootNode nodeVersion3 = new RootNode("2c36c55dd-956e-4b78-18c4-eef7e56aa17", 2);
        nodeVersion3.nodeType(BasicPage.TYPE);
        nodeVersion3.published(DateTime.now().plusMonths(3).toDate());
        nodeVersion3.create();

        BasicPage pageVersion3 = new BasicPage();
        pageVersion3.nodeId = nodeVersion3.nodeId();
        pageVersion3.version = nodeVersion3.version();
        pageVersion3.title = "Start Page";
        pageVersion3.leadReferenceId = lead.identifier;
        pageVersion3.bodyReferenceId = bodyVersion3.identifier;
        pageVersion2.create();

        // /start -> page 1
        Alias start = new Alias("start", nodeVersion2.nodeId());
        start.create();

    }

    private static void createPage2() {
        Content lead = new Content();
        lead.identifier = "e1497e18-fc78-3198-bac0-d2b1db6e4be0";
        lead.value = "Yip wiggle the Antichrist... bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>\n" +
                "Zongle yip flob boo dong flub zip dubba? Yap flabbing blobdabba! Ho hum Cartman quibbleflibble! Tingle dee dabba dingle? Twaddle quabble wiggle doo yada rizzle dongle hum dabba. Tangle zangle bam flip boo fling? Da cake abracadabra? \"Roo whack nip?\" wooble wackoongle.<br/>\n";
        lead.create();

        Content body = new Content();
        body.identifier = "d52ce214-48c0-4049-b94f-da81b3218536";
        body.value = "Zap sloppy razzleflap! Nip doo bizzlewibble dee duh flobble, zip bleebity duh blab da. \"Doo flong dee?\" nizzle Kyle. Goblin weeble ha bling da shnizzle? Dazzledee-blobbing-doof! Do-da woogle blung! Ho dobba Kyle...blingity blongity flubblung. Da bam Chaka Khan zonkwaggle!<br/>\n" +
                "Bleebity janglewoogle. Nip flupping flunging blungity bam boo zap crongely wogglenizzle. Da cringle flooblecake. Duh boo cringleflong loo dee zonk, zip flobbity zip fraggle bam. Bam shnoz yip shnozzle raz-ma tangblip, dee blong dizzle roo flibbity bingyada wubble. Twaddle bleep linghizzle, \"tingle da ha blung,\" nip Rev. Lovejoy shnaz razzle boo blung-wubble...tang dee boo! Whack boo waggle ho zangle-dobba!! Blee boo flobble-nizzle.<br/>\n" +
                "Blong zip goblin loo zangle ling duh dabba? Flanging razzzingle. \"Loo dongle duh?\" blup wobbleling. Ha flobbity flongity zingity loo dong nip crangely onglezingle. Hum blopping shnuzzle goblin shnuzzle duh dong. Duh wubble yip shnizzle zungity blopmeep, duh oodle shrubbery zip shizzely blupdang flub. Hum zip The Honorable Mayor Marion Barry wiggleblap! Waggle da blip hum meep zonk duh blee?<br/>\n" +
                "Shnuzzle yada ting loo shnazzle zung shnuzzle zap bloo. Boo blabbity flobbing shruberific ho blip da dongely shnuzzlequibble. Shnozzy bla tizzle! Bizzle zowee doo quabble boo twiddle? Blob loo jongely blingwoogle. Ha plop shnoz hizzyyada hum yip roo \"bluppity wooble-twiddle\". Rizzle blee? Flobble bing zip blung duh ding?<br/>\n" +
                "Zungle bam blob flibble? Hum hum doo Principal Skinner blameep. Tang! Bling bling tizzleabracadabra, \"zing yip nip blap,\" yap zunk shrubbery oodle boo woogle-dubba...flop nip zap! Da blo dobba dongleflop doo boo zap \"zappity zoom-zing\". Bam loo weeble zung flanging shizzely bleeb blangcringle? Blip zong zoom yap zoom ding hizzy ho flup. Crongely bleeb razzle!\n";
        body.create();

        RootNode node = new RootNode("c9615819-0556-4e70-b6a9-a66c5b8d4c1a", 1);
        node.nodeType(BasicPage.TYPE);
        node.create();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = "Page Not Found";
        page.themeVariant = "bootstrap-main_only";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.create();

        // ### Alias ###
        // /page-not-found -> page 2
        Alias pageNotFoundAlias = new Alias("page-not-found", node.nodeId());
        pageNotFoundAlias.create();

    }

    private static void createPage3() {
        Content lead = new Content();
        lead.identifier = "56798e9b-a9db-42d4-8806-3bfae8d9828c";
        lead.value = "Razzle dee crongely zoomzunk. Hizzy cake yap zingle duh dobba? Zungity wubbledongle. Oodle yip weeble-flip.<br/>\n" +
                "Flab flab blupping doo weebleflub??? Flobble da woggle-shnozzle. Bleep flub dee wibble hum flong? Doof nip zongity blipzonk. Tangle gobble flungity zip hizzleflong???<br/>\n";
        lead.create();

        Content body = new Content();
        body.identifier = "bc15887a-d4f4-4b6f-a3d8-437559971ade";
        body.value = "Ha gobble fling? Boo nip Trotsky floodazzle! Zangboo-bleebity-dazzle! Zangle dee tingle yip bing zongle duh plop? Flap zong? Tizzle fling? Loo shnizzle yip blong bleebing twaddlewhack, zap bananarama wacko ha blingity abracadabraflee tang. Yap bam tangle whack ting a blingity blo tongleflap?<br/>\n" +
                "Loo zang Stan...zingity flonging blupzong. \"Zip bleep hum?\" noodle tingweeble. Abracadabra zangle dingleplop, \"flup ho yip fling,\" bam crangle woogle hizzle boo flop-shnuzzle...zongle zip ho! Hizzy! Meep rizzle crungely zap tanglebizzle??? Blo bla dang loo slap bloo dang loo flip. Cake shnuzzle zangle zap wacko dubba bleeb roo bang. Loo zungle blopflobble.<br/>\n" +
                "Zoom ho ongle hum flibble bizzle doo dongle? Yap oodle Mr. Hat...flangity jingely izzlewaggle. Shruberific caketangle. Zing zap blap bleeb. \"Hum zowee ho?\" izzle dilznoofusdizzle. Noodle wobble bing boo crongle blob dingle doo crongle. Blob doo zowee-twaddle. Jackson roo tang roo blip blingity wugglezang.\n";
        body.create();

        RootNode node = new RootNode("1cf699a7-a0c4-4be0-855f-466042a36a8d", 1);
        node.nodeType(BasicPage.TYPE);
        node.create();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = "Internal Server Error";
        page.themeVariant = "bootstrap-main_only";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.create();

        // /error -> page 3
        Alias errorAlias = new Alias("error", node.nodeId());
        errorAlias.create();

    }

    private static void createPage4() {

        Content content_side_2 = new Content();
        content_side_2.identifier = "239945c1-8735-4a41-b81c-f825bf140ae6";
        content_side_2.value = "Boo crangle Miss Beasley... zonkity flobbing zinglemeep. Rizzle wheezer wibblequibble, \"kanoodle zap yip zang,\" zap Jackson wheezer flup loo flong-bananarama...blang loo da! Bam yip zip Clinton yadazangle.<br/> " +
                "Noodle crungle? Bang zing hum fraggle nip wubble? Zip roo twaddle shnuzzle flobbity flopping whack fragglemeep?";
        content_side_2.create();

        Content content_main_1 = new Content();
        content_main_1.identifier = "fc787e18-e149-4be0-bac0-d2b1db6e3198";
        content_main_1.value = "Yip wiggle the Antichrist... bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>" +
                "Zongle yip flob boo dong flub zip dubba? Yap flabbing blobdabba! Ho hum Cartman quibbleflibble! Tingle dee dabba dingle? Twaddle quabble wiggle doo yada rizzle dongle hum dabba. Tangle zangle bam flip boo fling? Da cake abracadabra? \"Roo whack nip?\" wooble wackoongle.<br/>" +
                "Fluppity zonk bleeb! Razzle ha crongle! Bang da crangle! Tizzle boo? Blab dobba wobble dee dubba bing shnozzle doo zoom. Dabba bam flib-flobble. Flub dee dang bananarama? Jangle shnoz flangity loo wobblezong???<br/>" +
                "Zunk flup bloptizzle, \"twaddle nip yap woogle,\" yip Chef yada bleeb yip wubble-floo...blab roo duh! Flanders yip Stan doo flipping izzle-tingle. Dee ho meep razz zang a dingely gobble razzledoof? Zingle hum zowee nip zung-zangle!! Marge hum OJ yip bleepity zowee-zangle. Dee duh shnozflung hum yip flip, nip flupping ho floo yap. Doo ho Clinton crangleflab! \"Doo bananarama yip?\" ling You.<br/>" +
                "Principal Skinner bam ling ha zongle blabbing razzcrongle. Doo razz bing razzleflooble ha boo hum \"flappity ling-ting\". Flibdee-blangity-jingle! Blob zungle? Shnazzle bling da waggle nip dobba? Tizzle dizzle bingblo, \"flap yap boo zing\", yip whack blab flibble duh hizzy-flop...oodle hum ho! Yap ingle zangle? Flubbing bleepwhack.";
        content_main_1.create();

        Content content_side_3 = new Content();
        content_side_3.identifier = "9769374a-36e2-4fcf-a0ea-8577b3d45954";
        content_side_3.value = "Dubba loo bling! Hizzy dee flip fraggle. Flappity whackboo.<br/>" +
                "\"Nip boo da?\" dang TIMMY. Slapyip-tang a-dingle! Dilznoofus dee bloppity bananaramawoggle.";
        content_side_3.create();

        RootNode node1 = new RootNode("aa1755dd-18c4-4b78-956e-eef7e562c36c", 1);
        node1.nodeType(StructuredPage.TYPE);
        node1.create();

        RootNode node2 = new RootNode("aa1755dd-18c4-4b78-956e-eef7e562c36c", 2);
        node2.nodeType(StructuredPage.TYPE);
        node2.create();

        StructuredPage page1 = new StructuredPage(); // Page 1 version 1
        page1.nodeId = node1.nodeId();
        page1.version = node1.version();
        page1.title = "Fourth Page";
        page1.create();

        StructuredPage page2 = new StructuredPage(); // Page 1 version 2
        page2.nodeId = node2.nodeId();
        page2.version = node2.version();
        page2.title = "Fourth Page";
        page2.create();

        // Page 1 -> Segment
        Segment segment_1_1 = new Segment(); // Page 1 version 1, Segment 1
        segment_1_1.nodeId = node1.nodeId();
        segment_1_1.version = node1.version();
        segment_1_1.type = Content.TYPE;
        segment_1_1.referenceId = content_side_2.identifier;
        segment_1_1.create();

        Segment segment_1_2 = new Segment(); // Page 1 version 1, Segment 2
        segment_1_2.nodeId = node1.nodeId();
        segment_1_2.version = node1.version();
        segment_1_2.type = Content.TYPE;
        segment_1_2.referenceId = content_main_1.identifier;
        segment_1_2.create();

        Segment segment_1_3 = new Segment(); // Page 1 version 1, Segment 3
        segment_1_3.nodeId = node1.nodeId();
        segment_1_3.version = node1.version();
        segment_1_3.type = Content.TYPE;
        segment_1_3.referenceId = content_side_2.identifier;
        segment_1_3.create();

        Segment segment_2_1 = new Segment(); // Page 1 version 2, Segment 1
        segment_2_1.nodeId = node2.nodeId();
        segment_2_1.version = node2.version();
        segment_2_1.type = Content.TYPE;
        segment_2_1.referenceId = content_side_3.identifier;
        segment_2_1.create();

        Segment segment_2_2 = new Segment(); // Page 1 version 2, Segment 2
        segment_2_2.nodeId = node2.nodeId();
        segment_2_2.version = node2.version();
        segment_2_2.type = Content.TYPE;
        segment_2_2.referenceId = content_main_1.identifier;
        segment_2_2.create();

        // Page 1 -> Meta
        Meta meta_1_1 = new Meta(); // Page 1 Version 1 Segment 1 Meta
        meta_1_1.nodeId = segment_1_1.referenceId;
        meta_1_1.version = segment_1_1.version;
        meta_1_1.weight = 100;
        meta_1_1.region = "left";
        meta_1_1.referenceId = content_side_2.identifier;
        meta_1_1.create();

        Meta meta_1_2 = new Meta(); // Page 1 Version 1 Segment 2 Meta
        meta_1_2.nodeId = node1.nodeId();
        meta_1_2.version = node1.version();
        meta_1_2.weight = 100;
        meta_1_2.region = "main";
        meta_1_2.referenceId = content_main_1.identifier;
        meta_1_2.create();

        Meta meta_2_1 = new Meta(); // Page 1 Version 2 Segment 1 Meta
        meta_2_1.nodeId = page2.nodeId();
        meta_2_1.version = page2.version();
        meta_2_1.weight = 50;
        meta_2_1.region = "left";
        meta_2_1.referenceId = content_side_2.identifier;
        meta_2_1.create();

        Meta meta_2_2 = new Meta(); // Page 1 Version 2 Segment 2 Meta
        meta_2_2.nodeId = page2.nodeId();
        meta_2_2.version = page2.version();
        meta_2_2.weight = 100;
        meta_2_2.region = "left";
        meta_2_2.referenceId = content_side_3.identifier;
        meta_2_2.create();

        Meta meta_2_3 = new Meta(); // Page 1 Version 2 Segment 3 Meta
        meta_2_3.nodeId = page2.nodeId();
        meta_2_3.version = page2.version();
        meta_2_3.weight = 100;
        meta_2_3.region = "main";
        meta_2_3.referenceId = content_main_1.identifier;
        meta_2_3.create();

        // /fourth -> page 4
        Alias fourth = new Alias("fourth", node2.nodeId());
        fourth.create();

    }

    private static void createPage5() {

        Content lead = new Content();
        lead.identifier = "244ee89f-5edb-4f6d-9544-c9f67a6b9ea6";
        lead.value = "Goblin abracadabra dobbawacko, \"whack roo dee shnazzle,\" boo shnozzle wuggle fling nip razzle-wacko...crungle hum dee! Flung bam dizzle loo bleeb shnaz nip ingle? Zonkha-izzle-boo!";
        lead.create();

        Content body = new Content();
        body.identifier = "18cb6c2b-70eb-4ca4-a96b-b97772f4564f";
        body.value = "Blung wooble duh tizzle bam wiggle? Ho nip doo Principal Skinner shnuzzlecringle. Flung yap bling boo crongle-blob!! Zap zap da Chaka Khan zonkshnazzle. Dee boo Smithers flibzung! Blob ha blang zip flap twaddle dee blob? Flib da zongity blooflee. Dingleloo-zapping-bling!\n\n\"Bam ingle da?\" slop flibfloo. Da yip yap Kenny wubbletang. Yap blab goblinwiggle. Ha crangle hum wobble cringely wogglewibble, loo flang razz roo slappy shnizzlecrangle zoom. Jingle flee cringlewacko, \"blung ha duh bang,\" roo blee tongle tingle loo razz-zang...hizzy zap duh! \"Boo dobba dee?\" quibble razzwacko. Flob hum hizzle duh wubble bizzle yap blee? Zip ha Luke zonkshnazzle!";
        body.create();

        RootNode node = new RootNode("699eb321-7545-4b27-8a7f-94a4442d2046", 1);
        node.nodeType(BasicPage.TYPE);
        node.create();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = "Protected Page";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.create();

        // /protected -> page 5
        Alias protectedAlias = new Alias("protected", node.nodeId());
        protectedAlias.create();

    }

    private static void createPage6() {

        Content lead = new Content();
        lead.identifier = "92e9b4f7-0269-48b7-81b6-fe4186754481";
        lead.value = "Goblin abracadabra dobbawacko, \"whack roo dee shnazzle,\" boo shnozzle wuggle fling nip razzle-wacko...crungle hum dee! Flung bam dizzle loo bleeb shnaz nip ingle? Zonkha-izzle-boo!";
        lead.create();

        Content body = new Content();
        body.identifier = "ad3c9bbe-d9fe-4f89-bca7-daedb8d16da7";
        body.value = "Blung wooble duh tizzle bam wiggle? Ho nip doo Principal Skinner shnuzzlecringle. Flung yap bling boo crongle-blob!! Zap zap da Chaka Khan zonkshnazzle. Dee boo Smithers flibzung! Blob ha blang zip flap twaddle dee blob? Flib da zongity blooflee. Dingleloo-zapping-bling!\n\n\"Bam ingle da?\" slop flibfloo. Da yip yap Kenny wubbletang. Yap blab goblinwiggle. Ha crangle hum wobble cringely wogglewibble, loo flang razz roo slappy shnizzlecrangle zoom. Jingle flee cringlewacko, \"blung ha duh bang,\" roo blee tongle tingle loo razz-zang...hizzy zap duh! \"Boo dobba dee?\" quibble razzwacko. Flob hum hizzle duh wubble bizzle yap blee? Zip ha Luke zonkshnazzle!";
        body.create();

        RootNode node = new RootNode("f4501c31-690f-46f4-853d-167165a4fc03", 1);
        node.nodeType(BasicPage.TYPE);
        node.create();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = "Unauthorized Access";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.create();

        // /error -> page 6
        Alias unauthorizedAlias = new Alias("unauthorized", node.nodeId());
        unauthorizedAlias.create();

    }

    private static void createPage7() {

        Content lead = new Content();
        lead.identifier = "60ba72eb-b00f-4100-abb1-6b13d24a4db3";
        lead.value = "This page has a component in the body of the page.";
        lead.create();

        Content body = new Content();
        body.identifier = "d7728926-3e27-44a6-be71-8c2d1c95c2ae";
        body.value = Component.COMPONENT_MARKER;
        body.create();

        RootNode node = new RootNode("807f2ece-c143-4f32-88db-1e1dfcd3e2d9", 1);
        node.nodeType(BasicPage.TYPE);
        node.create();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = "Component Test Page";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.create();

        // /component -> page 7 (component)
        Alias component = new Alias("component", node.nodeId());
        component.create();
    }

    private static void createPage8() {

        Content lead = new Content();
        lead.identifier = "a6fbcc47-57c9-45bb-b96a-c8d376095385";
        lead.value = "<p>Preview Tickets are used to view how the content will be displayed at a certain date and time.</p>";
        lead.create();

        Content body = new Content();
        body.identifier = "c26ba92a-9cec-43ae-b63f-6e5c4f6b1eb9";
        body.value = Component.COMPONENT_MARKER;
        body.create();

        RootNode node = new RootNode("2ef4a79b-15f2-4d8d-a100-54d0331f8a51", 1);
        node.nodeType(BasicPage.TYPE);
        node.create();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId();
        page.version = node.version();
        page.title = "Preview Ticket";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.create();

        // /preview -> page 7 (component)
        Alias alias1 = new Alias("preview", node.nodeId());
        alias1.create();

        // /preview -> page 7 (component)
        Alias alias2 = new Alias("preview/submit", node.nodeId());
        alias2.create();

        // /preview -> page 7 (component)
        Alias alias3 = new Alias("preview/remove", node.nodeId());
        alias3.create();
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

}
