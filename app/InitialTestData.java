import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.*;
import models.origo.core.navigation.AliasNavigation;
import models.origo.core.navigation.BasicNavigation;
import models.origo.core.navigation.ExternalLinkNavigation;
import models.origo.core.navigation.PageIdNavigation;
import models.origo.structuredcontent.Segment;
import models.origo.structuredcontent.StructuredPage;

public class InitialTestData {

    public void create() {
        if (Settings.load().getValue(CoreSettingsHelper.Keys.BASE_URL) == null) {
            createSettings();
            createPage1();
            createPage2();
            createPage3();
            createPage4();
            createAliases();
            createNavigation();
        }
    }

    private void createSettings() {
        Settings settings = Settings.load();
        settings.setValueIfMissing(CoreSettingsHelper.Keys.BASE_URL, "/");
        settings.setValueIfMissing(CoreSettingsHelper.Keys.START_PAGE, "aa1755dd-18c4-4b78-956e-eef7e562c36c"); // Page 1
        settings.setValueIfMissing(CoreSettingsHelper.Keys.PAGE_NOT_FOUND_PAGE, "c9615819-0556-4e70-b6a9-a66c5b8d4c1a"); // Page 2
        settings.setValueIfMissing(CoreSettingsHelper.Keys.INTERNAL_SERVER_ERROR_PAGE, "1cf699a7-a0c4-4be0-855f-466042a36a8d"); // Page 3
        settings.save();
    }

    private void createPage1() {

        Content content_side_2 = new Content();
        content_side_2.identifier = "239945c1-8735-4a41-b81c-f825bf140ae6";
        content_side_2.value = "Boo crangle Miss Beasley... zonkity flobbing zinglemeep. Rizzle wheezer wibblequibble, \"kanoodle zap yip zang,\" zap Jackson wheezer flup loo flong-bananarama...blang loo da! Bam yip zip Clinton yadazangle.<br/> " +
                "Noodle crungle? Bang zing hum fraggle nip wubble? Zip roo twaddle shnuzzle flobbity flopping whack fragglemeep?";
        content_side_2.save();

        Content content_main_1 = new Content();
        content_main_1.identifier = "fc787e18-e149-4be0-bac0-d2b1db6e3198";
        content_main_1.value = "Yip wiggle the Antichrist... bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>" +
                "Zongle yip flob boo dong flub zip dubba? Yap flabbing blobdabba! Ho hum Cartman quibbleflibble! Tingle dee dabba dingle? Twaddle quabble wiggle doo yada rizzle dongle hum dabba. Tangle zangle bam flip boo fling? Da cake abracadabra? \"Roo whack nip?\" wooble wackoongle.<br/>" +
                "Fluppity zonk bleeb! Razzle ha crongle! Bang da crangle! Tizzle boo? Blab dobba wobble dee dubba bing shnozzle doo zoom. Dabba bam flib-flobble. Flub dee dang bananarama? Jangle shnoz flangity loo wobblezong???<br/>" +
                "Zunk flup bloptizzle, \"twaddle nip yap woogle,\" yip Chef yada bleeb yip wubble-floo...blab roo duh! Flanders yip Stan doo flipping izzle-tingle. Dee ho meep razz zang a dingely gobble razzledoof? Zingle hum zowee nip zung-zangle!! Marge hum OJ yip bleepity zowee-zangle. Dee duh shnozflung hum yip flip, nip flupping ho floo yap. Doo ho Clinton crangleflab! \"Doo bananarama yip?\" ling You.<br/>" +
                "Principal Skinner bam ling ha zongle blabbing razzcrongle. Doo razz bing razzleflooble ha boo hum \"flappity ling-ting\". Flibdee-blangity-jingle! Blob zungle? Shnazzle bling da waggle nip dobba? Tizzle dizzle bingblo, \"flap yap boo zing\", yip whack blab flibble duh hizzy-flop...oodle hum ho! Yap ingle zangle? Flubbing bleepwhack.";
        content_main_1.save();

        Content content_side_3 = new Content();
        content_side_3.identifier = "9769374a-36e2-4fcf-a0ea-8577b3d45954";
        content_side_3.value = "Dubba loo bling! Hizzy dee flip fraggle. Flappity whackboo.<br/>" +
                "\"Nip boo da?\" dang TIMMY. Slapyip-tang a-dingle! Dilznoofus dee bloppity bananaramawoggle.";
        content_side_3.save();

        RootNode node1 = new RootNode("aa1755dd-18c4-4b78-956e-eef7e562c36c", 1);
        node1.nodeType = "models.origo.structuredcontent.StructuredPage";
        node1.save();

        RootNode node2 = new RootNode("aa1755dd-18c4-4b78-956e-eef7e562c36c", 2);
        node2.nodeType = "models.origo.structuredcontent.StructuredPage";
        node2.save();

        StructuredPage page1 = new StructuredPage(); // Page 1 version 1
        page1.nodeId = node1.getNodeId();
        page1.version = node1.getVersion();
        page1.title = "Start Page";
        page1.save();

        StructuredPage page2 = new StructuredPage(); // Page 1 version 2
        page2.nodeId = node2.getNodeId();
        page2.version = node2.getVersion();
        page2.title = "Start Page";
        page2.save();

        // Page 1 -> Segment
        Segment segment_1_1 = new Segment(); // Page 1 version 1, Segment 1
        segment_1_1.nodeId = node1.getNodeId();
        segment_1_1.version = node1.getVersion();
        segment_1_1.type = "models.origo.core.Content";
        segment_1_1.referenceId = content_side_2.identifier;
        segment_1_1.save();

        Segment segment_1_2 = new Segment(); // Page 1 version 1, Segment 2
        segment_1_2.nodeId = node1.getNodeId();
        segment_1_2.version = node1.getVersion();
        segment_1_2.type = "models.origo.core.Content";
        segment_1_2.referenceId = content_main_1.identifier;
        segment_1_2.save();

        Segment segment_1_3 = new Segment(); // Page 1 version 1, Segment 3
        segment_1_3.nodeId = node1.getNodeId();
        segment_1_3.version = node1.getVersion();
        segment_1_3.type = "models.origo.core.Content";
        segment_1_3.referenceId = content_side_2.identifier;
        segment_1_3.save();

        Segment segment_2_1 = new Segment(); // Page 1 version 2, Segment 1
        segment_2_1.nodeId = node2.getNodeId();
        segment_2_1.version = node2.getVersion();
        segment_2_1.type = "models.origo.core.Content";
        segment_2_1.referenceId = content_side_3.identifier;
        segment_2_1.save();

        Segment segment_2_2 = new Segment(); // Page 1 version 2, Segment 2
        segment_2_2.nodeId = node2.getNodeId();
        segment_2_2.version = node2.getVersion();
        segment_2_2.type = "models.origo.core.Content";
        segment_2_2.referenceId = content_main_1.identifier;
        segment_2_2.save();

        // Page 1 -> Meta
        Meta meta_1_1 = new Meta(); // Page 1 Version 1 Segment 1 Meta
        meta_1_1.nodeId = segment_1_1.referenceId;
        meta_1_1.version = segment_1_1.version;
        meta_1_1.weight = 100;
        meta_1_1.region = "left";
        meta_1_1.referenceId = content_side_2.identifier;
        meta_1_1.save();

        Meta meta_1_2 = new Meta(); // Page 1 Version 1 Segment 2 Meta
        meta_1_2.nodeId = node1.getNodeId();
        meta_1_2.version = node1.getVersion();
        meta_1_2.weight = 100;
        meta_1_2.region = "main";
        meta_1_2.referenceId = content_main_1.identifier;
        meta_1_2.save();

        Meta meta_2_1 = new Meta(); // Page 1 Version 2 Segment 1 Meta
        meta_2_1.nodeId = page2.getNodeId();
        meta_2_1.version = page2.getVersion();
        meta_2_1.weight = 50;
        meta_2_1.region = "left";
        meta_2_1.referenceId = content_side_2.identifier;
        meta_2_1.save();

        Meta meta_2_2 = new Meta(); // Page 1 Version 2 Segment 2 Meta
        meta_2_2.nodeId = page2.getNodeId();
        meta_2_2.version = page2.getVersion();
        meta_2_2.weight = 100;
        meta_2_2.region = "left";
        meta_2_2.referenceId = content_side_3.identifier;
        meta_2_2.save();

        Meta meta_2_3 = new Meta(); // Page 1 Version 2 Segment 3 Meta
        meta_2_3.nodeId = page2.getNodeId();
        meta_2_3.version = page2.getVersion();
        meta_2_3.weight = 100;
        meta_2_3.region = "main";
        meta_2_3.referenceId = content_main_1.identifier;
        meta_2_3.save();

    }

    private void createPage2() {
        Content lead = new Content();
        lead.identifier = "e1497e18-fc78-3198-bac0-d2b1db6e4be0";
        lead.value = "Yip wiggle the Antichrist... bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>\n" +
                "Zongle yip flob boo dong flub zip dubba? Yap flabbing blobdabba! Ho hum Cartman quibbleflibble! Tingle dee dabba dingle? Twaddle quabble wiggle doo yada rizzle dongle hum dabba. Tangle zangle bam flip boo fling? Da cake abracadabra? \"Roo whack nip?\" wooble wackoongle.<br/>\n";
        lead.save();

        Content body = new Content();
        body.identifier = "d52ce214-48c0-4049-b94f-da81b3218536";
        body.value = "Zap sloppy razzleflap! Nip doo bizzlewibble dee duh flobble, zip bleebity duh blab da. \"Doo flong dee?\" nizzle Kyle. Goblin weeble ha bling da shnizzle? Dazzledee-blobbing-doof! Do-da woogle blung! Ho dobba Kyle...blingity blongity flubblung. Da bam Chaka Khan zonkwaggle!<br/>\n" +
                "Bleebity janglewoogle. Nip flupping flunging blungity bam boo zap crongely wogglenizzle. Da cringle flooblecake. Duh boo cringleflong loo dee zonk, zip flobbity zip fraggle bam. Bam shnoz yip shnozzle raz-ma tangblip, dee blong dizzle roo flibbity bingyada wubble. Twaddle bleep linghizzle, \"tingle da ha blung,\" nip Rev. Lovejoy shnaz razzle boo blung-wubble...tang dee boo! Whack boo waggle ho zangle-dobba!! Blee boo flobble-nizzle.<br/>\n" +
                "Blong zip goblin loo zangle ling duh dabba? Flanging razzzingle. \"Loo dongle duh?\" blup wobbleling. Ha flobbity flongity zingity loo dong nip crangely onglezingle. Hum blopping shnuzzle goblin shnuzzle duh dong. Duh wubble yip shnizzle zungity blopmeep, duh oodle shrubbery zip shizzely blupdang flub. Hum zip The Honorable Mayor Marion Barry wiggleblap! Waggle da blip hum meep zonk duh blee?<br/>\n" +
                "Shnuzzle yada ting loo shnazzle zung shnuzzle zap bloo. Boo blabbity flobbing shruberific ho blip da dongely shnuzzlequibble. Shnozzy bla tizzle! Bizzle zowee doo quabble boo twiddle? Blob loo jongely blingwoogle. Ha plop shnoz hizzyyada hum yip roo \"bluppity wooble-twiddle\". Rizzle blee? Flobble bing zip blung duh ding?<br/>\n" +
                "Zungle bam blob flibble? Hum hum doo Principal Skinner blameep. Tang! Bling bling tizzleabracadabra, \"zing yip nip blap,\" yap zunk shrubbery oodle boo woogle-dubba...flop nip zap! Da blo dobba dongleflop doo boo zap \"zappity zoom-zing\". Bam loo weeble zung flanging shizzely bleeb blangcringle? Blip zong zoom yap zoom ding hizzy ho flup. Crongely bleeb razzle!\n";
        body.save();

        RootNode node = new RootNode("c9615819-0556-4e70-b6a9-a66c5b8d4c1a", 1);
        node.nodeType = "models.origo.core.BasicPage";
        node.themeVariant = "default-main_only";
        node.save();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId;
        page.version = node.version;
        page.title = "Page Not Found";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.save();

    }

    private void createPage3() {
        Content lead = new Content();
        lead.identifier = "56798e9b-a9db-42d4-8806-3bfae8d9828c";
        lead.value = "Razzle dee crongely zoomzunk. Hizzy cake yap zingle duh dobba? Zungity wubbledongle. Oodle yip weeble-flip.<br/>\n" +
                "Flab flab blupping doo weebleflub??? Flobble da woggle-shnozzle. Bleep flub dee wibble hum flong? Doof nip zongity blipzonk. Tangle gobble flungity zip hizzleflong???<br/>\n";
        lead.save();

        Content body = new Content();
        body.identifier = "bc15887a-d4f4-4b6f-a3d8-437559971ade";
        body.value = "Ha gobble fling? Boo nip Trotsky floodazzle! Zangboo-bleebity-dazzle! Zangle dee tingle yip bing zongle duh plop? Flap zong? Tizzle fling? Loo shnizzle yip blong bleebing twaddlewhack, zap bananarama wacko ha blingity abracadabraflee tang. Yap bam tangle whack ting a blingity blo tongleflap?<br/>\n" +
                "Loo zang Stan...zingity flonging blupzong. \"Zip bleep hum?\" noodle tingweeble. Abracadabra zangle dingleplop, \"flup ho yip fling,\" bam crangle woogle hizzle boo flop-shnuzzle...zongle zip ho! Hizzy! Meep rizzle crungely zap tanglebizzle??? Blo bla dang loo slap bloo dang loo flip. Cake shnuzzle zangle zap wacko dubba bleeb roo bang. Loo zungle blopflobble.<br/>\n" +
                "Zoom ho ongle hum flibble bizzle doo dongle? Yap oodle Mr. Hat...flangity jingely izzlewaggle. Shruberific caketangle. Zing zap blap bleeb. \"Hum zowee ho?\" izzle dilznoofusdizzle. Noodle wobble bing boo crongle blob dingle doo crongle. Blob doo zowee-twaddle. Jackson roo tang roo blip blingity wugglezang.\n";
        body.save();

        RootNode node = new RootNode("1cf699a7-a0c4-4be0-855f-466042a36a8d", 1);
        node.nodeType = "models.origo.core.BasicPage";
        node.themeVariant = "default-main_only";
        node.save();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId;
        page.version = node.version;
        page.title = "Internal Server Error";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.save();

    }

    private void createPage4() {

        Content lead = new Content();
        lead.identifier = "2dd34035-7940-42c5-aadc-9710153fc7c9";
        lead.value = "Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin.";
        lead.save();

        Content body = new Content();
        body.identifier = "8659c030-6fd5-4d8e-8304-9671d4e5857f";
        body.value = "Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo." +
                "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! Zoom blang loo flee dee bloo? Jangely bleeb twaddle!" +
                "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! \"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh." +
                "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!";
        body.save();

        RootNode node = new RootNode("2c36c55dd-956e-4b78-18c4-eef7e56aa17", 1);
        node.nodeType = "models.origo.core.BasicPage";
        node.save();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId;
        page.version = node.version;
        page.title = "Fourth Page";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.save();

    }

    private void createAliases() {

        // ### Alias ###
        // /page-not-found -> page 2
        Alias pageNotFoundAlias = new Alias("page-not-found", "c9615819-0556-4e70-b6a9-a66c5b8d4c1a");
        pageNotFoundAlias.save();

        // /error -> page 3
        Alias errorAlias = new Alias("error", "1cf699a7-a0c4-4be0-855f-466042a36a8d");
        errorAlias.save();

        // /fourth -> page 4
        Alias fourth = new Alias("fourth", "2c36c55dd-956e-4b78-18c4-eef7e56aa17");
        fourth.save();

    }

    private void createNavigation() {
        // Start Page
        BasicNavigation startNavigation = new BasicNavigation();
        startNavigation.type = "models.origo.core.navigation.PageIdNavigation";
        startNavigation.section = "front";
        startNavigation.referenceId = "3f2d9e2e-12dc-4917-9a58-40d325e9784e";
        startNavigation.weight = 1;
        startNavigation.save();
        PageIdNavigation startPageId = new PageIdNavigation();
        startPageId.identifier = startNavigation.getReferenceId();
        startPageId.pageId = "aa1755dd-18c4-4b78-956e-eef7e562c36c";
        startPageId.save();

        // Fourth
        BasicNavigation fourthNavigation = new BasicNavigation();
        fourthNavigation.type = "models.origo.core.navigation.AliasNavigation";
        fourthNavigation.section = "front";
        fourthNavigation.referenceId = "a8129a97-70fa-40b7-93e4-2a1caf181a0d";
        fourthNavigation.weight = 3;
        fourthNavigation.save();
        AliasNavigation fourthAlias = new AliasNavigation();
        fourthAlias.identifier = fourthNavigation.getReferenceId();
        fourthAlias.alias = "fourth";
        fourthAlias.save();

        // External - Google
        BasicNavigation externalNavigation = new BasicNavigation();
        externalNavigation.type = "models.origo.core.navigation.ExternalLinkNavigation";
        externalNavigation.section = "front";
        externalNavigation.referenceId = "58e15bfa-da4f-4f6b-a15a-51ab6c82e670";
        externalNavigation.parent = fourthNavigation;
        externalNavigation.weight = 2;
        externalNavigation.save();
        ExternalLinkNavigation external = new ExternalLinkNavigation();
        external.identifier = externalNavigation.getReferenceId();
        external.title = "Google";
        external.link = "http://www.google.com";
        external.save();
    }

}
