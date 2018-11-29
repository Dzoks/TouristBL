package org.unibl.etf.mr.touristbl;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;


import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.unibl.etf.mr.touristbl.fragment.EventFragment;

import org.unibl.etf.mr.touristbl.fragment.FavoriteFragment;
import org.unibl.etf.mr.touristbl.fragment.HomeFragment;
import org.unibl.etf.mr.touristbl.fragment.HotelFragment;
import org.unibl.etf.mr.touristbl.fragment.InstitutionFragment;
import org.unibl.etf.mr.touristbl.fragment.MapFragment;
import org.unibl.etf.mr.touristbl.fragment.NewsFragment;
import org.unibl.etf.mr.touristbl.fragment.SightseeFragment;
import org.unibl.etf.mr.touristbl.fragment.WeatherFragment;
import org.unibl.etf.mr.touristbl.model.Entry;
import org.unibl.etf.mr.touristbl.model.NewsDetails;
import org.unibl.etf.mr.touristbl.util.EntryDAO;
import org.unibl.etf.mr.touristbl.util.EntryDB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private static EntryDB entryDB;
    private ImageButton settingButton;
    private static CircularFifoQueue<NewsDetails> newsCache;
    private static Menu mainMenu;

    public static void setNewsCache(CircularFifoQueue<NewsDetails> newsCache) {
        MainActivity.newsCache = newsCache;
    }

    public static CircularFifoQueue<NewsDetails> getNewsCache() {
        return newsCache;
    }

    public static EntryDB getEntryDB() {
        return entryDB;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        entryDB=Room.databaseBuilder(getApplicationContext(), EntryDB.class, "tourist-db").build();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        selectLanguage(PreferenceManager.getDefaultSharedPreferences(this).getString("language_list",""));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                fillData();
                return null;
            }
        }.execute();
        setContentView(R.layout.activity_main);
        mToolbar=findViewById(R.id.toolbar);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        mNavigationView=findViewById(R.id.nav_view);
        settingButton=findViewById(R.id.setting_button);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });




        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragment()).commit();
        findViewById(R.id.search_entry).setVisibility(View.GONE);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof HomeFragment))
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container, new HomeFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_news:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof NewsFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container, new NewsFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_institutions:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof InstitutionFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new InstitutionFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_sightsee:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof SightseeFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new SightseeFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_hotels:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof HotelFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new HotelFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_events:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof EventFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new EventFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_weather:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof WeatherFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container, new WeatherFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_map:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof MapFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new MapFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_fav:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof FavoriteFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new FavoriteFragment()).addToBackStack(null).commit();
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void fillData() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("firstrun", true)) {
            fillHotels();
            fillInstitutions();
            fillLandmarks();
            fillEvents();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("firstrun", false).apply();
        }
    }

    private void fillHotels() {
        String magic="HOTEL";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Hotel Bosna");
        entry1.setDescription("\t  \t<p>Kralja Petra I Karađorđevića 97<br />Tel: +387(0)51 215 775<br />Email:<a href=\"mailto:info@hotelbosna.com\"> info@hotelbosna.com</a><a href=\"mailto:info@hotelbosna.com\"><br /></a>Web:<a href=\"http://www.hotelbosna.com\"> www.hotelbosna.com<br /></a></p>\n" +
                "        <p style=\"text-align: justify;\"><span style=\"text-align: justify;\"><br />Hotel Bosna je jedan od najstarijih hotela u Bosni i Hercegovini. Počeo je sa radom 1885 godine uz tada&scaron;nju željezničku prugu koja je vodila do željezničke stanice u centru grada. Hotel Bosna je i danas jedan od najprestižnijih hotela u Banjaluci. U sklopu hotela se nalaze Piano- bar, restoran, kafići sa ba&scaron;tom, disko klub, kazino, butici i drugi sadržaji.</span></p>");
        entry1.setLat(44.7731236);
        entry1.setLng(17.1921137);
        entry1.setImage(R.drawable.ho_bosna);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Motel Nana");
        entry2.setDescription("<p>Ivana Gorana Kovačića 211a<br />Tel: +387(0)51 370 471<br />Email:<a href=\"mailto:nanamotel@yahoo.com\"> nanamotel@yahoo.com</a><br />Web:<a href=\"http://www.motelnana.com\" target=\"_blank\"> www.motelnana.com</a></p>\n" +
                "\t  \t\n" +
                "<p style=\"text-align: justify;\"><br />Smje&scaron;ten je na magistralnom putu Lakta&scaron;i - Banja Luka - Jajce. Motel Nana ima ljetnu terasu sa 180 mjesta. Pogodan za razne skupove, seminare, svadbe, zabave.</p>");
        entry2.setLat(44.8054147);
        entry2.setLng(17.1988231);
        entry2.setImage(R.drawable.ho_nana);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Hotel Atina");
        entry3.setDescription("<p>Slobodana Kokanovića 5<br />Tel: +387(0)51 961 100<br />Email:<a href=\"mailto:recepcija@atinahotel.com\"> recepcija@atinahotel.com<br /></a>Web:<a href=\"http://www.atinahotel.com\"> www.atinahotel.com<br /></a><a href=\"http://%20www.atinahotel.com\" target=\"_blank\"></a></p>             \n" +
                "    <p style=\"text-align: justify;\"><span style=\"text-align: justify;\">Hotel \"Atina\" se nalazi u neposrednoj blizini poslovnog centra grada, na mirnom i tihom mjestu uz rijeku Vrbas. Hotel nudi klimatizovane sobe i apartmane, kao i predsjedničke apartmane. Raspolaže sa 16 soba i 4 apartmana u kojima je raspoređeno 46 kreveta.</span></p>\n" +
                "  ");
        entry3.setLat(44.7670103);
        entry3.setLng(17.1942233);
        entry3.setImage(R.drawable.ho_atina);

        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }
    private void fillInstitutions(){
        String magic="INSTITUTION";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Narodna Skupština Republike Srpske");
        entry1.setDescription("<p>Narodna skupština Republike Srpske je zakonodavni organ u strukturi vlasti Republike Srpske.  Obavljajući normativne, kontrolne, regulatorne i izborne funkcije iz svoje nadležnosti, centralna je institucija parlamentarnog uređenja Republike Srpske.</p>\n" +
                "<p class=\"rtejustify\">Narodna skupština počela je sa radom 24.oktobra 1991. godine pod nazivom Skupština srpskog naroda Bosne i Hercegovine. U to vrijeme, administrativno sjedište skupštine bilo je u Sarajevu.    </p>\n" +
                "<p class=\"rtejustify\">Neposredno  nakon  izbijanja ratnog sukoba, sjedište skupštine izmješteno je u Pale.</p>\n" +
                "<p class=\"rtejustify\">Od 1998. godine, administrativno sjedište Narodne skupštine je u Banjaluci, gdje se nalazi i danas.</p>\n" +
                "<p class=\"rtejustify\">Poslanici Narodne skupštine 9. januara 1992. godine usvojili su Deklaraciju o proglašenju Republike srpskog naroda Bosne i Hercegovine. Ovaj dan se u Republici Srpskoj obilježava kao Dan Republike.</p>\n" +
                "<p class=\"rtejustify\">Narodna skupština je 28. februara 1992. godine donijela Ustav, kao utemeljenje pravne moći, sigurnosti, legaliteta i legitimiteta Republike Srpske.</p>\n" +
                "<p class=\"rtejustify\">Po okončanju ratnih dejstava -  a nakon potpisivanja Dejtonskog mirovnog sporazuma 1995. godine - Narodna skupština preuzima primarnu odgovornost sprovođenja normativne državne reizgradnje i obnove.</p>\n" +
                "<p class=\"rtejustify\">Prema savremenim parlamentarnim principima, sastav skupštine čine 83 narodna poslanika, izabrana na neposrednim parlamentarnim izborima.</p>\n" +
                "<p class=\"rtejustify\">Prvi poslijeratni izbori održani su 1996. godine. Do tada, skupštinu su činili poslanici koji su bili izabrani na prvim višestranačkim izborima u BiH 1990. godine, kao poslanici tadašnje Skupštine BiH.  </p>\n" +
                "<p class=\"rtejustify\">Do 2002. godine, narodni poslanici birani su na mandat u trajanju od dvije godine. Od 2002. godine, poslanici se biraju na četvorogodišnji mandat.</p>\n" +
                "<p>Zgrada Narodne skupštine Republike Srpske nalazi se u Trg jasenovačkih žrtava 1 u Banjaluci. Nakon katastrofalnog zemljotresa, koji je pogodio Banjaluku 1969. godine, izgrađeno je više objekata u samom središtu grada, a jedno od najljepših je nekadašnji Dom Vojske, a danas  sjedište Narodne skupštine Republike Srpske.</p>\n" +
                "<p>Dom Vojske svečano je otvoren 30. juna 1973. godine, u okviru proslave Dana borca (4. juli) u prisustvu brojnih uglednih zvaničnika i umjetničkih stvaralaca.  Dobio je status najljepšeg kulturnog zdanja u Banjaluci koji je raspolagao sa više od 7000 m² prostora, između ostalog sa koncertnom salom s 500 sjedišta, velikom dvoranom za sastanke i konferencije, više učionica, čitaonicom i bibliotekom, galerijom sa izložbenim prostorom, ljetnom kino-baštom sa 400 sjedišta, atrijumom i plesnim prostorom i drugim sadržajima.</p>\n" +
                "<p>Zdanje Doma JNA bilo je mjesto u kojem se njegovala kultura i širila prosvjetna misija sve do ratnih dešavanja na našim prostorima 1992. godine.</p>\n" +
                "<p>Tadašnji Dom Vojske, svojom dispozicijom i hortikulturnim uređenjem s fontanom, činilo je veoma uspješno arhitektonsko ostvarenje banjalučkog arhitekte Kasima Osmančevića.</p>\n" +
                "<p>Od 2000. godine, nakon početne faze rekonstrukcije enterijera, zdanje je postalo sjedište Narodne skupštine Republike Srpske što je omogućilo daleko bolje uslove za parlamentarne aktivnosti, kako u pogledu savremeno opremljene sale za plenarne sjednice, tako i za rad poslaničkih klubova i odbora.</p>\n" +
                "<p>Prostor u kom su se održavale brojne kulturno-zabavne manifestacije danas je sala za plenarne sjednice parlamenta, a u  bivšoj biblioteci na 2. spratu nalaze se kancelarije i sale za sjednice odbora Narodne skupštine.</p>\n" +
                "<p>Od tada do danas,  prostor se s jedne strane prilagođava potrebama Narodne skupštine, a s druge imperativima koje nameću civilizacijski standardi, kao što su savremeno opremljena govornica sa prostorom za predstavnike sredstava informisanja, uklanjanje arhitektonskih prepreka i slično.</p>\n" +
                "<p>Ispred zgrade Narodne skupštine je 2007. godine, u znak sjećanja na žrtve ustaškog logora smrti Jasenovac, podignut spomenik ''Topola užasa'', skluptura koja je evidentirana kao dio ambijentalne cjeline ulica Kralja Petra I Karađorđevića i Mladena Stojanovića i uvrštena na privremenu listu nacionalnih spomenika BiH, koji se nalazi u I zoni zaštite.</p>\n" +
                "<p>Sala u kojoj se održavaju zasjedanja Narodne skupštine opremljena je savremenim konferencijskim sistemom sa elektronskim glasanjem koji omogućava, u tehničkom pogledu brz i efikasan rad.</p>\n" +
                "<p>Ugrađena oprema podržava rad 114 učesnika, omogućava prikazivanje rezultata glasanja i vrijeme diskusije, prikazuje poziciju aktivnih konferencijskih jedinica, a takođe omogućava ozvučenje prostora. Računarski konferencijski sistem pruža mogućnost efikasnog rada, čuvanja i štampanja potrebnih podataka. Dodatna ugrađena oprema omogućava snimanje audio i video zapisa na odgovarajući medij.</p>\n" +
                "<p>Pored sale u kojoj se održava sjednica Narodne skupštine nalaze se pres hol i pres soba koji su na usluzi sredstvima informisanja za praćenje rada Narodne skupštine Republike Srpske.</p>");
        entry1.setLat(44.7746068);
        entry1.setLng(17.1932777);
        entry1.setImage(R.drawable.in_assembly);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Akademija nauka i umjetnosti Republike Srpske");
        entry2.setDescription("<p>Akademija nauka i umjetnosti Republike Srpske (ANURS) je najviša naučna, kulturna, radna i reprezentativna ustanova u Republici Srpskoj. Zadatak ove akademske institucije je da razvija, unapređuje i podstiče naučnu i umjetničku djelatnost. Akademija je ustanova od posebnog nacionalnog interesa za Republiku Srpsku.</p>\n" +
                "<p>Predsednik Akademije je akademik Rajko Kuzmanović, potpredsednik je akademik Ljubomir Zuković, a generalni sekretar je akademik Dragoljub Mirjanić. Pored navedenih, predsedništvo Akademije čine i akademik Veselin Perić, akademik Slobodan Remetić, akademik Branko Škundrić, akademik Drenka Šećerov-Zečević i dopisni član Rade Mihaljčić. Akademija u svom sastavu ima četiri glavna odjeljenja, odbore, centre i institute u kojima radi oko 150 naučnika. Sjedište Akademije nauka i umjetnosti Republike Srpske se nalazi na Trgu srpskih vladara 2/II, u Banjaluci. </p>");
        entry2.setLat(44.7722771);
        entry2.setLng(17.1935177);
        entry2.setImage(R.drawable.in_anurs);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Muzej Republike Srpske");
        entry3.setDescription("<p style=\"text-align: justify;\">Osnovan 26. septembra 1930. pod nazivom Muzej Vrbaske banovine. Po nalogu Kralja Aleksandra Karađorđevića, osnivač muzeja je bio prvi ban Vrbaske banovine Svetislav Tisa Milosavljević. Prvi upravnik Muzeja Vrbaske banovine bio je Spiridon &Scaron;piro Bocarić, akademski slikar iz Budve. Za prvih deset godina rada (do 1941. godine) skupio je u okolini Banja Luke veliki broj etnografskih predmeta neprocjenjive istorijske, nacionalne i kulturne vrijednosti. Do 1982. godine Muzej RS je nekoliko puta mijenjao naziv i prostor, a potom smje&scaron;ten u dijelu objekta Doma radničke solidarnosti, u kojem su Narodna i univerzitetska biblioteka i dječije pozori&scaron;te. Odlukom Vlade Republike Srpske, 14. novembra 1992. godine, dotada&scaron;nji Muzej Bosanske Krajine preimenovan je u Muzej Republike Srpske i progla&scaron;en centralnom ustanovom za&scaron;tite pokretnih kulturnih dobara. Danas je smje&scaron;ten u objektu Doma radničke solidarnosti i raspolaže sa 3. 700 m<sup>2</sup> vlastitog prostora od čega se na 1.500 m<sup>2</sup> nalazi Stalna izložbena postavka, a ostali dio depoi i službene prostorije.</p>\n" +
                "<p>U svojim zbirkama Muzej RS čuva vi&scaron;e od 30.000 eksponata. Specijalizovana muzejska biblioteka raspolaže sa vi&scaron;e od 14.000 knjiga. Adaptacijom prostora Muzej RS je dobio prostor za autorske, temtske i gostujuće izložbe.</p>\n" +
                "<p>&nbsp;Zanimljivo je da u Muzeju RS možete pronaći i zbirku predmeta sa ostrva Fidži u Melaneziji. Ono po čemu je ova zbirka interesantna jeste da sadrži razne domorodačke predmete: buzdovane, &scaron;titove, lukove, vesla, nakit i dr. </p>\n" +
                "<p><strong>Muzej Republike Srpske</strong><br />Đure Daničića 1<br />Tel: +387(0)51 215 973<br />Email:&nbsp;<a href=\"mailto:muzejrs@inecco.net\">muzejrs@inecco.net</a> <br />Web:&nbsp;<a href=\"http://www.muzejrs.com\">www.muzejrs.com</a></p>");
        entry3.setLat(44.7691033);
        entry3.setImage(R.drawable.in_museum);
        entry3.setLng(17.1914287);
        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }
    private void fillLandmarks(){
        String magic="SIGHTSEE";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Tvrđava Kastel");
        entry1.setDescription("\n" +
                "<p style=\"text-align: justify;\">Tvrđava Kastel je najstariji istorijski spomenik u gradu Banjaluci. Najstariji tragovi naselja na području Banjaluke su ostaci neolitskog gradinskog naselja koji su upravo pronađeni na prostoru gradske tvrđave Kastel. Nalazi se u sredi&scaron;njem dijelu grada, koji dominira lijevom obalom Vrbasa.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Pretpostavke o nastanku tvrđave</strong></p>\n" +
                "<p style=\"text-align: justify;\">O vremenu nastanka ovog objekta ne postoje pouzdani podaci. Međutim, mnoge okolnosti upućuju na zaključak da se upravo tu nalazilo rimsko naselje Castra. Rimljani su bili izloženi čestim nasrtajima varvarskih naroda, a imali su svakako dovoljne jake razloge da brane cestu koja je prolazila kotlinom Vrbasa. U prilog ovakvim tvrdnjama idu i arheolo&scaron;ki nalazi koji su pronađeni na prostoru dana&scaron;njeg Kastela, a odnose se na rimsku keramiku, novac i građevinarstvo. Posebno važan nalaz je antički žrtvenik posvećen bogu Jupiteru pronađen 1885. godine prilikom gradnje mosta preko Crkvene. Takođe, na prostoru tvrđave pronađeni su ostaci Slavenskog naselja iz perioda ranog srednjeg vijeka (od VIII do XII vijeka). U bosanskoj srednjovjekovnoj državi postojao je Vrba&scaron;ki grad, koji se po jednoj pretpostavci nalazio na mjestu dana&scaron;njeg Kastela. Međutim, po drugoj pretpostavci, Vrba&scaron;ki grad se identifikuje sa Podgradcima u Potkozarju.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Intenzivna gradnja tvrđave</strong></p>\n" +
                "<p style=\"text-align: justify;\">Intezivna izgradnja Kastela počinje u pretposljednjoj deceniji XV vijeka, za vrijeme Turske okupacije i vladavine Ferhad pa&scaron;e Sokolovića (1574-1588) koji pored ovog utvrđenja gradi i niz drugih objekata orijentalnog tipa. Ferhad- pa&scaron;a na mjestu dana&scaron;njeg Kastela prvo gradi svoju utvrđenu tophanu (arsenal), da bi, desetak godina kasnije, tophana prerasla u pravi utvrđeni grad sa kulama i tabijama, koji se neprestano dograđivao. Po&scaron;to leži na u&scaron;ću Crkvene u Vrbas, od tvrđave su bila podignuta dva mosta. Jedan je i&scaron;ao preko Vrbasa, u blizini sada&scaron;njeg Gradskog mosta, a drugi preko Crkvene. Most preko Vrbasa sačuvao se samo u jednoj staroj gravuri. Vrbas i Crkvenu povezivao je veliki opkop (&scaron;anac) tako da je tvrđava zapravo, svojevremeno, bila utvrđeno ostrvo kojeg su okruživale vode dviju rijeka. Na zidovima tvrđave je takođe bio i veliki drveni konak koji je gledao na Vrbas, kao i niz zidanih objekata iz perioda dok je u tvrđavi bila austrougarska vojska. Tvrđava ali i grad Banja Luka posebno dobijaju na značaju za vrijeme Austro-Ugarsko-Turskih ratova kao važan geostrate&scaron;ki centar.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Tvrđava danas</strong></p>\n" +
                "<p style=\"text-align: justify;\">U planovima za sanaciju, restauraciju i revitalizaciju Kastela predviđeno je da se jedan dio ovih objekata obnovi, kako bi tvrđava dobila izgled &scaron;to približniji nekada&scaron;njem. Na prostoru same tvrđave nađeno je dosta arheolo&scaron;kih tragova, iz antičkih vremena, preko već spominjane slavenske nastambe do novijih kultura. U Kastelu se nalazi rimski sarkofag nađen u &Scaron;argovcu, rimski miljokaz s nekada&scaron;njeg puta Salona- Servitium. Danas je tvrđava Kastel spomenik I kategorije.&nbsp;<span style=\"text-align: justify;\">U pro&scaron;losti Kastel je bio jako vojničko utvrđenje i &scaron;titio je kotlinu Vrbasa od neprijateljskih naleta. Tvrđava je sa svih strana opasana debelim kamenim zidovima, a u njenoj unutra&scaron;njosti, pored ljetne pozornice, igrali&scaron;ta za djecu i nacionalnog restorana nalazi se i Kamena kuća u funkciji muzeja. <br /></span></p>\t  </div>\n" +
                "\t  \t  \t  <br>\n");
        entry1.setLat(44.7666);
        entry1.setLng(17.1907);
        entry1.setImage(R.drawable.la_kastel);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Hram Hrista Spasitelja");
        entry2.setDescription("<p style=\"text- align: justify;\"><strong>Izgradnja crkve</strong></p>\n" +
                "<p style=\"text-align: justify;\">Izgradnja Saoborne crkve \"Svete Trojice\" u centru grada je bio prvi veći graditeljski poduhvat u Banjaluci nakon Prvog svjetskog rata. Građena je na praznom prostoru ispred tada&scaron;nje kafane &laquo;Balkan&raquo; od 1925. godine. Projektovao ju je Du&scaron;an Živanović, arhitekta iz Beograda.Crkva je izrađena u srpskovizantijskom stilu, koji se kod nas javlja u arhitektonskoj praksi krajem XIX i početkom XX vijeka. Građevinski, crkva je zavr&scaron;ena 1929. godine, a enterijer je rađen poslije raspisanog jugoslovenskog konkursa, na kojem je nagradu dobio Grigorije Samojlov. Ukra&scaron;avanje unutra&scaron;njosti crkve i izrada ikonostasa povjereni su Jovanu Bijeliću, Veljku Stanojeviću, Svetislavu Strali, Jaroslavu Kratini i Petru Suhačevu. Crkvi je kumovao prvi ban Vrbaske banovine Svetislav Tisa Milosavljević 1929. godine. Izgradnja Saborne crkve iniciraće dalju izgradnju okolnog prostora, a posebno u periodu Vrbaske banovine.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Bombardovanje crkve</strong></p>\n" +
                "<p style=\"text-align: justify;\">Crkva je o&scaron;tećena u nacističkom bombardovanju Banjaluke 1941. godine, a ubrzo zatim i sru&scaron;ena po nalogu tada&scaron;nje okupatorske vlasti Nezavisne države Hrvatske. Po zavr&scaron;etku II svjetskog rata na ovom mjestu je izgrađen spomenik palim borcima u Narodno oslobodilačkoj borbi protiv fa&scaron;izma.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Obnova crkve</strong></p>\n" +
                "<p style=\"text-align: justify;\">Sa obnovom crkve, sada pod nazivom, Hrista Spasitelja se počelo 1993. a temelji su iskopani 1992. godine. Spomenik palim borcima u Narodno- oslobodilačkoj borbi je izmje&scaron;ten na plato ispred hotela Bosna. Hram se gradio od 1993 do 2004. godine. Urađen je troslojan zid kao specifičan način gradnje. Crkva je sagrađena od najplemenitijeg kamena sa Bliskog istoka tzv. TRAVERTIN u bojama (crveni i žuti) koji nastaje vulkanskim erupcijama. Na fasadi hrama se nalaze portali, rozete, stubovi, krstovi, bifole arhivolte koje su rađene od kararskog bijelog mermera. Stubovi (6 velikih i 4 mala) su napravljeni od granita iz mjesta Đadone, Sardinija. Pozlaćeni lim kojim su pokrivene kupole ne rđa, a izrađen je po ruskoj tehnologiji. Zvona su izrađena u Insbruku kao &scaron;to su bila zvona i u poru&scaron;enoj crkvi (B dur) 6400T te&scaron;ka, a najveće zvono je te&scaron;ko 3200T. Ugrađena je i najsavremenija elektronika za zvonjenje. Iako jo&scaron; uvjek njena unutra&scaron;njost nije uređena, danas crkva Hrista Spasitelja predstavlja jednu od najljep&scaron;ih i najvećih arhitektonskih ostvarenja pravoslavnih hramova na Balkanu.</p>\n" +
                "<p>Vi&scaron;e na <a href=\"http://hhsbl.org/\">web stranici</a>.</p>\t  </div>\n" +
                "\t  \t  \t  <br>");
        entry2.setLat(44.7722714);
        entry2.setLng(17.1916723);
        entry2.setImage(R.drawable.la_hram);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Banj brdo");
        entry3.setDescription("\n" +
                "<p style=\"text-align: justify;\"><strong>Lokacija spomenika</strong></p>\n" +
                "<p style=\"text-align: justify;\">Spomenik palim Kraji&scaron;nicima, u Narodno-oslobodilačkoj borbi (1941-1945) protiv fa&scaron;izma, nalazi se na vrhu Banj brda (431m nadmorske visine) koje dominira Banjalukom. Udaljen je oko 5km od Grada, a put je cjelom dužinom asfaltiran.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Opis spomenika</strong> <br />Spomenik, mauzolejskog tipa (visine 13 m i dužine 24 m), rad je jednog od najvećih jugoslovenskih kipara i majstora memorijalne plastike Antuna Augustinčića. Podignut je na mjestu gdje je, juna 1941, održana Oblasna konferencija Komunističke partije Jugoslavije za Bosansku krajinu i nekoliko savjetovanja vezanih za podizanje ustanka. Izgrađen je od bijelog bračkog kamena.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p style=\"text-align: justify;\">Ima jedinstvenu formu, posebnu dinamičku liniju i gledan iz daljine, djeluje kao metak ispaljen u pravcu Krajine (Kozare i Grmeča).Na pročelju spomenika, iznad ulaznih vrata, nalazi se velika figura nagog mladića koji u ruci drži zastavu. Sa strana Spomenika nalaze se reljefi na kojima je prikazana borba protiv okupatora i poslijeratna izgradanja.</p>\n" +
                "<p style=\"text-align: justify;\"><strong>Legenda o &Scaron;ehitima</strong></p>\n" +
                "<p>Brdo &Scaron;ehitluci, sa kojeg se pruža vidikovac na centar Banjaluke, dobilo je ime po trojici junaka. Legenda govori o junacima, kojima su u boju odsječene glave sabljama.&nbsp; No, iako su izgubili glave, oni su nastavili put vrha brda zajedno sa glavama u rukama, zastali su na jednom vrelu kako bi oprali svoje okrvavljene vratove, po čemu je vrelo i&nbsp; dobilo ime Vratinac. Izdahnuli su na vrhu brda, na mjestu dana&scaron;njeg spomenika. Po drugoj verziji ove legende, na tom brdu visokom 431 m, sahranjena su dvojica od trojice turskih &scaron;ehita (junaka), koji su u boju 1737. godine, posječeni pod Kastelom, ali su se sa galavama u rukama nastavili penjati. Jedan do njih se okrenuo ka gradu i presvisnuo, a preostala dvojica su onako obezglavljena dojahala na vrh brda i tek tamo izdahnuli. Pokopani su u zajednički grob mauzolej, a brdo je po njima nazvano &Scaron;ehitluci.</p>\t  </div>\n" +
                "\t  \t  \t  <br>");
        entry3.setLat(44.7436879);
        entry3.setLng(17.1633191);
        entry3.setImage(R.drawable.la_banj);

        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }
    private void fillEvents(){
        String magic="EVENT";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Festival domaćih proizvoda, ostrvo Stara Ada - 13.12.2018.");
        entry1.setDescription("<p>Festival domaćih proizvoda je događaj na kojem ćete imati priliku upoznati, degustirati i saznati mnogo toga o proizvodnom asortimanu više od 40 različitih proizvođača iz cijele Republike Srpske!" +
                "</p><p>" +
                "Proizvođači vina, rakije, piva, organskih proizvoda, domaći poznati brendovi iz oblasti hrane i pića su samo dio velikog događaja koji nas očekuje 13. decembra na ostrvu Stara Ada (svečani salon).\n" +
                "</p><p>" +
                "Naravno, festival ne može da prođe bez muzičkog sadržaja, takođe domaćeg karaktera. Za ugodnu atmosferu zadužen je Lukijan Ivanović sa bendom.</p>");
        entry1.setLat(44.770578);
        entry1.setLng(17.218776);
        entry1.setImage(R.drawable.ev_food);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Koncert Željka Joksimovića - 01.01.2019.");
        entry2.setDescription("<p>Željko Joksimović je objavio svoj novi spot za pjesmu “Ponelo me” koju će sa publikom prvi put zajedno pjevati u Banja Luci na reprizi nove 2019 i pocetak nove godine na svom velikom koncertu koji će se održati na Trgu Krajine pod pokroviteljstvom Grada Banja Luke s početkom u 21 sat.\n" +
                "Radi se o veseloj, plesnoj pjesmi koja dočarava pravu ljetnu atmosferu, a kakav će zapravo biti i 2019 godina.\n" +
                "</p>\n" +
                "<p>\n" +
                "“ Publika može očekivati koncert sa brzim pjesmama jer je ljeto period kada ljudi vole plesati, opustiti se I uživati. Ovo je idealna prilika da uđemo u novu godinu i novu sezonu sa jednim dobrim glazbenim koncertom kakav će biti u Banja Luci 01.01.2019. Pozivam ljude da dođu u što većem broju, što nas više bude to ćemo podijeliti ljepšu energiju i pamtiti ovaj koncert zauvijek”, kazao je Željko Joksimović.</p>");
        entry2.setLat(44.7697388);
        entry2.setLng(17.1898314);
        entry2.setImage(R.drawable.ev_zeljko);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("MakeITwork 2019 - 18-19.05.2019.");
        entry3.setDescription("<span>Barijera između IT studenata i kompanija je velika. <br><br>Naše rješenje?<br><br>Događaj koji će na jednom mjestu okupiti studente, mlade profesionalce, IT entuzijaste i predstavnike kompanija iz regiona, te stvoriti ugodnu i opuštenu atmosferu za druženje, razmjenu informacija i networking. <br><br>Predstavljamo vam makeITwork 2019.<br>makeITwork je dvodnevni IT sajam čija je osnovna ideja okupljanje predstavnika kompanija i institucija sa djelovanjem u oblasti informacionih tehnologija, inženjeringa i obrazovanja, te studenata i IT entuzijaste oko raznovrsnih aktivnosti poput promocija kompanija, predavanja, radionica, panel diskusija i brojnih drugih.<br><br>Cilj sajma je ublaživanje barijere između kompanija i mladih ljudi zainteresovanih za izlazak na tržište rada, te razmjena informacija, iskustava i kontakata.<br><br>Događaj će se održati 18. i 19. maja 2019. godine u Banjoj Luci, kao dio Konferencije medicinskog i bioinženjeringa - CMBEBiH 2019.<br><br>Želite se povezati sa studentima i mladim IT entuzijastima i iz prve ruke saznati pitanja koja ih muče?<br><br>Želite li otvoriti vrata svoje kompanije i predstaviti se na kreativan način?<br><br>Sva pitanja možete poslati na našu službenu e-mail adresu banjaluka@eestec.net<br><br>Čekamo vas.<br>Let’s makeITwork!</span>");
        entry3.setLat(44.7690419);
        entry3.setLng(17.1912888);
        entry3.setImage(R.drawable.ev_it);
        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cache_switch",false)) {
            try {
                FileInputStream fin = openFileInput("cache");
                ObjectInputStream oin = new ObjectInputStream(fin);
                newsCache = (CircularFifoQueue<NewsDetails>) oin.readObject();

                oin.close();
            } catch (IOException | ClassNotFoundException e) {
                newsCache = new CircularFifoQueue<>(20);
            }
        }


    }
    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    protected void onStop() {

        super.onStop();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cache_switch",false)){
            try {
                FileOutputStream fin= openFileOutput("cache",Context.MODE_PRIVATE);
                ObjectOutputStream os=new ObjectOutputStream(fin);
                os.writeObject(newsCache);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
