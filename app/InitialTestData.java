import models.origo.core.Settings;
import models.origo.core.*;

public class InitialTestData {

    public void create() {
        if (Settings.load().id == null) {
            createSettings();
            createPage1();
            createPage2();
            createPage3();
        }
    }

    private void createSettings() {
        Settings settings = Settings.load();
        settings.setValue(SettingsKeys.Core.BASE_URL, "/");
        settings.setValue(SettingsKeys.Core.START_PAGE, "aa1755dd-18c4-4b78-956e-eef7e562c36c"); // Page 1
        settings.setValue(SettingsKeys.Core.PAGE_NOT_FOUND_PAGE, "c9615819-0556-4e70-b6a9-a66c5b8d4c1a"); // Page 2
        settings.setValue(SettingsKeys.Core.INTERNAL_SERVER_ERROR_PAGE, "1cf699a7-a0c4-4be0-855f-466042a36a8d"); // Page 3
        settings.setValue(SettingsKeys.Core.THEME_VARIANT, "default-main_and_left_columns");
        settings.save();
    }

    private void createPage1() {

        Content lead = new Content();
        lead.identifier = "2dd34035-7940-42c5-aadc-9710153fc7c9";
        lead.value = "Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin.";
        lead.save();

        Content body = new Content();
        body.identifier = "8659c030-6fd5-4d8e-8304-9671d4e5857f";
        body.value = "Bam loo blong woogle bleebing rakity flakity crongle quabbleflup? Duh blap twaddle? Hum bam weeble flip tangity flapping flub blingdubba? Nip bam tingleingle ho doo kanoodle, zap shnozzy hum cringle boo. \"Dee yada ho?\" blo wheezeryada. Dubbaloo-dangely-dang! \"Yip bananarama yip?\" flop Chef. Flong kanoodle blab roo blab gobble blob hum goblin." +
                "Yip dee doof blong sloppy flabbing blob wooglezangle? Razz boo blaoodle, \"flong dee zap izzle,\" zap flob blab doof roo wibble-zang...boo dee ho! Hizzle ha weeble hizzy. Bam blipping blippity zupping doo blup zap oodely zingwobble. Shnazzle boo zong zip bleeb boo ha blip? Lisa zap Mr. Garrison zap tongity plop-zung. Woggle yip zung abracadabra. Zing da kanoodle-blo."+
                "Hum duh hizzlebling ho loo dabba, boo flabbity nip zingle hum. Zip zing a zinghizzy! Hum dilznoofus flib? Duh dongely blingity flanging dee blap da blabbing bingbleeb. Crungle gobble bleebity yap noodlequabble??? Blippity hizzy oodle! Zoom blang loo flee dee bloo? Jangely bleeb twaddle!"+
                "Shizzle fling funknizzle, \"zonk duh yap funk,\" doo Belgium gobble shnozzle nip flop-blop...flobble zip hum! Yip da Smithers goblindongle! Flung yap shizzle crongle. Blob dee crongle bang? Nip zap Cartman dingling! \"Boo flooble da?\" zung zoweebizzle. Bam ho woogle twaddle crangely oodely zung dangding? Dee duh razzleflob loo ho bananarama, boo flipping ha blop duh."+
                "Mr. Slave yap Mr. Slave doo nippy blap-dazzle. Slop ho zowee roo slap-flobble!! Dazzle blo shnizzleblip, \"shnuzzle bam dee shizzle,\" doo zangle razz gobble dee blop-meep...kanoodle ho duh! Doo zongle shnizzlewow. Ho flob woggle? Quabble dee blab flibble? Slop crungle doo whack ho dizzle? Funk blee blangfloo, \"bla doo dee wooble,\" ho Mr. Slave dongle flee zip twiddle-razz...bing da nip!";
        body.save();

        RootNode node = new RootNode("aa1755dd-18c4-4b78-956e-eef7e562c36c", 1);
        node.nodeType = "models.origo.core.BasicPage";
        node.save();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId;
        page.version = node.version;
        page.title = "Start page";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.save();

    }

    private void createPage2() {
        Content lead = new Content();
        lead.identifier = "fc787e18-e149-4be0-bac0-d2b1db6e3198";
        lead.value = "Yip wiggle the Antichrist...bleebing zippity twiddletangle. Boo roo OJ ingleblong! Wiggle! Hizzy zangle bloodilznoofus, \"wooble roo bam wheezer,\" duh flee shnizzle dubba zip wiggle-quabble...wiggle boo da! Roo tongle zong? Dobba tangle dongely da cakefunk??? \"Bam razz nip?\" goblin Luke. Blee woggle flibbing ho shnizzleflibble???<br/>\n" +
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
        node.save();

        BasicPage page = new BasicPage();
        page.nodeId = node.nodeId;
        page.version = node.version;
        page.title = "Internal Server Error";
        page.leadReferenceId = lead.identifier;
        page.bodyReferenceId = body.identifier;
        page.save();

    }


}
