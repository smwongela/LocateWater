package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;

import com.boreholes.locatewater.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StepTwoRiversActivity extends AppCompatActivity {
    String post_key = null;
    String village = null;
    String sources =null;
    String fresh= null;
    String salty= null;
 //   String sourceType= null;

    String[] counties = {
            "Mombasa", "Kwale", "Kilifi", "Tana River", "Lamu", "Taita-Taveta", "Garissa", "Wajir", "Mandera", "Marsabit", "Isiolo", "Meru",
            "Tharaka-Nithi", "Embu", "Kitui", "Machakos", "Makueni", "Nyandarua", "Nyeri", "Kirinyaga",
            "Murang'a", "Kiambu", "Turkana", "West Pokot", "Samburu", "Trans-Nzoia", "Uasin Gishu",
            "Elgeyo-Marakwet", "Nandi", "Baringo", "Laikipia", "Nakuru", "Narok", "Kajiado", "Kericho", "Bomet",
            "Kakamega", "Vihiga", "Bungoma", "Busia", "Siaya", "Kisumu", "Homa Bay", "Migori", "Kisii",
            "Nyamira", "Nairobi"
    };
    String[] subcounties ={
            "Changamwe",	"Jomvu",	"Kisauni",	"Likoni",	"Mvita",	"Nyali",	"Kinango",	"Lungalunga",	"Matuga",	"Msambweni",
            "Samburu Kwale",	"Chonyi",	"Ganze",	"Kaloleni",	"Kauma",	"Kilifi North",	"Kilifi South",	"Magarini",	"Malindi",	"Rabai",
            "Tana North",	"Tana Delta",	"Tana River",	"Lamu East",	"Lamu West",	"Mwatate",	"Taita",	"Taveta",	"Voi",	"Balambala",
            "Dadaab",	"Fafi",	"Garissa",	"Hulugho",	"Ijara",	"Lagdera",	"Buna",	"Eldas",	"Habaswein.",	"Tarbaj",	"Wajir East.",
            "Wajir North",	"Wajir South",	"Wajir West",	"Mandera West.",	"Banisa",	"Kutulo",	"Lafey",	"Mandera Central",
            "Mandera East",	"Mandera North",	"Loiyangalani",	"Marsabit Central.",	"Marsabit North.",	"Marsabit South.",	"Moyale",
            "North Horr",	"Sololo",	"Garbatulla.",	"Isiolo",	"Merti",	"Buuri East",	"Buuri West.",	"Igembe Central",	"Igembe North",
            "Igembe South",	"Imenti North.",	"Imenti South.",	"Meru Central",	"Tigania Central",	"Tigania East.",	"Tigania West",
            "Meru National Park.",	"Mt Kenya Forest",	"Igambang'ombe",	"Maara",	"Meru South",	"Tharaka North",	"Tharaka South",
            "Mt Kenya Forest",	"Embu East",	"Embu North",	"Embu West",	"Mbeere South",	"Mbeere North",	"Mt Kenya Forest",	"Ikutha",
            "Katulani",	"Kisasi",	"Kitui Central",	"Kitui West",	"Kyuso",	"Lower Yatta",	"Matinyani",	"Migwani",	"Mumoni",
            "Mutitu",	"Mutitu North",	"Mutomo",	"Mwingi Central",	"Mwingi East",	"Nzambani",	"Thagicu",	"Tseikuru",	"Athi River",
            "Kalama",	"Kangundo",	"Kathiani",	"Machakos",	"Masinga",	"Matungulu",	"Mwala",	"Yatta",	"Kathonzweni",	"Kibwezi",
            "Kilungu",	"Makindu",	"Makueni",	"Mbooni East",	"Mbooni West",	"Mukaa",	"Nzaui",	"Kinangop",	"Nyandarua South",
            "Mirangine",	"Kipipiri",	"Nyandarua Central",	"Nyandarua West",	"Nyandarua North",	"Aberdare National Park",
            "Tetu",	"Kieni East",	"Kieni West",	"Mathira East",	"Mathira West",	"Nyeri South",	"Mukurwe ini",	"Nyeri Central",
            "Mt Kenya Forest",	"Aberdare Forest",	"Kirinyaga Central",	"Kirinyaga East",	"Kirinyaga West",	"Mwea East",
            "Mwea West",	"Mt Kenya Forest",	"Murang'a East",	"Kangema",	"Mathioya",	"Kahuro",	"Murang'a South",
            "Gatanga",	"Kigumo",	"Kandara",	"Aberdare Forest",	"Gatundu North",	"Gatundu South",	"Githunguri",
            "Juja",	"Kabete",	"Kiambaa",	"Kiambu",	"Kikuyu",	"Lari",	"Limuru",	"Ruiru",	"Thika East",
            "Thika West",	"Kibish",	"Loima",	"Turkana Central",	"Turkana East",	"Turkana North",	"Turkana South",
            "Turkana West",	"Kipkomo",	"Pokot Central",	"Pokot North",	"Pokot South",	"West Pokot",
            "Samburu Central",	"Samburu East",	"Samburu North",	"Trans Nzoia West",	"Trans Nzoia East",
            "Kwanza",	"Endebess",	"Kiminini",	"Ainabkoi",	"Kapseret",	"Kesses",	"Moiben",	"Soy",	"Turbo",
            "Keiyo North",	"Keiyo South",	"Marakwet East",	"Marakwet West",	"Chesumei",	"Nandi Central",
            "Nandi East",	"Nandi North",	"Nandi South",	"Tinderet",	"Baringo Central",	"Baringo North",
            "East Pokot",	"Koibatek",	"Marigat",	"Mogotio",	"Tiaty East",	"Laikipia Central",	"Laikipia East",
            "Laikipia North",	"Laikipia West",	"Nyahururu",	"Gilgil",	"Kuresoi North",	"Kuresoi South",
            "Molo",	"Naivasha",	"Nakuru East",	"Nakuru North",	"Nakuru West",	"Njoro",	"Rongai",	"Subukia",	"Narok East",
            "Narok North",	"Narok South",	"Narok West",	"Trans Mara East",	"Trans Mara West",	"Mau Forest",	"Isinya",
            "Kajiado Central",	"Kajiado North",	"Kajiado West",	"Loitokitok",	"Mashuuru",	"Belgut",	"Bureti",	"Kericho East",
            "Kipkelion",	"Londiani",	"Soin Sigowet",	"Bomet East",	"Chepalungu",	"Konoin",	"Sotik",	"Bomet Central",
            "Butere",	"Kakamega Central",	"Kakamega East",	"Kakamega North",	"Kakamega South",	"Khwisero",	"Likuyani",	"Lugari",
            "Matete",	"Matungu",	"Mumias East",	"Mumias West",	"Navakholo",	"Emuhaya",	"Vihiga",	"Sabatia",	"Luanda",
            "Hamisi",	"Kakamega Forest",	"Bumula",	"Bungoma Central",	"Bungoma East",	"Bungoma North",	"Bungoma South",
            "Cheptais",	"Kimilili Bungoma",	"Mt Elgon",	"Bungoma West",	"Tongaren",	"Webuye West",	"Mt Elgon Forest",	"Bunyala",
            "Busia",	"Butula",	"Nambale",	"Samia",	"Teso North",	"Teso South",	"Siaya",	"Gem",	"Ugenya",
            "Ugunja",	"Bondo",	"Rarieda",	"Kisumu East",	"Kisumu Central",	"Kisumu West",	"Seme",	"Muhoroni",	"Nyando",
            "Nyakach",	"Homa Bay",	"Ndhiwa",	"Rachuonyo North",	"Rachuonyo East",	"Rachuonyo South",	"Rangwe",
            "Suba North",	"Suba South",	"Awendo",	"Kuria East",	"Kuria West",	"Nyatike",	"Rongo",	"Suna East",
            "Suna West",	"Uriri",	"Etago",	"Gucha",	"Gucha South",	"Kenyenya",	"Kisii Central",	"Kisii South",
            "Kitutu Central",	"Marani",	"Masaba South",	"Nyamache",	"Sameta",	"Borabu",	"Manga",	"Masaba North",	"Nyamira North",
            "Nyamira South",	"Dagoretti",	"Embakasi",	"Kamukunji",	"Kasarani",	"Kibra",	"Lang'ata",	"Makadara",	"Mathare",
            "Njiru",	"Starehe",	"Westlands",


    };
    String [] wards ={
            "Port Reitz",	"Kipevu",	"Airport",	"Changamwe",	"Chaani",	"Jomvu Kuu",	"Miritini",	"Mikindani",	"Mjambere",	"Junda",	"Bamburi",	"Mwakirunge",	"Mtopanga",	"Magogoni",	"Shanzu",	"Frere Town",	"Ziwa La Ng'Ombe",	"Mkomani",	"Kongowea",	"Kadzandani",	"Mtongwe",	"Shika Adabu",	"Bofu",	"Likoni",	"Timbwani",	"Mji Wa Kale/Makadara",	"Tudor",	"Tononoka",	"Shimanzi/Ganjoni",	"Majengo",	"Gombatobongwe",	"Ukunda",	"Kinondo",	"Ramisi",	"Pongwekikoneni",	"Dzombo",	"Mwereni",	"Vanga",	"Tsimba Golini",	"Waa",	"Tiwi",	"Kubo South",	"Mkongani",	"Nadavaya",	"Puma",	"Kinango",	"Mackinnon-Road",	"Chengoni/Samburu",	"Mwavumbo",	"Kasemeni",	"Tezo",	"Sokoni",	"Kibarani",	"Dabaso",	"Matsangoni",	"Watamu",	"Mnarani",	"Junju",	"Mwarakaya",	"Shimo La Tewa",	"Chasimba",	"Mtepeni",	"Mariakani",	"Kayafungo",	"Kaloleni",	"Mwanamwinga",	"Mwawesa",	"Ruruma",	"Kambe/Ribe",	"Rabai/Kisurutini",	"Ganze",	"Bamba",	"Jaribuni",	"Sokoke",	"Jilore",	"Kakuyuni",	"Ganda",	"Malindi Town",	"Shella",	"Marafa",	"Magarini",	"Gongoni",	"Adu",	"Garashi",	"Sabaki",	"Kipini East",	"Garsen South",	"Kipini West",	"Garsen Central",	"Garsen West",	"Garsen North",	"Kinakomba",	"Mikinduni",	"Chewani",	"Wayu",	"Chewele",	"Bura",	"Bangale",	"Sala",	"Madogo",	"Faza",	"Kiunga",	"Basuba",	"Shella",	"Mkomani",	"Hindi",	"Mkunumbi",	"Hongwe",	"Witu",	"Bahari",	"Chala",	"Mahoo",	"Bomeni",	"Mboghoni",	"Mata",	"Wundanyi/Mbale",	"Werugha",	"Wumingu/Kishushe",	"Mwanda/Mgange",	"Rong'E",	"Mwatate",	"Bura",	"Chawia",	"Wusi/Kishamba",	"Mbololo",	"Sagalla",	"Kaloleni",	"Marungu",	"Kasigau",	"Ngolia",	"Waberi",	"Galbet",	"Township",	"Iftin",	"Balambala",	"Danyere",	"Jara Jara",	"Saka",	"Sankuri",	"Modogashe",	"Benane",	"Goreale",	"Maalimin",	"Sabena",	"Baraki",	"Dertu",	"Dadaab",	"Labasigale",	"Damajale",	"Liboi",	"Abakaile",	"Bura",	"Dekaharia",	"Jarajila",	"Fafi",	"Nanighi",	"Hulugho",	"Sangailu",	"Ijara",	"Masalani",	"Gurar",	"Bute",	"Korondile",	"Malkagufu",	"Batalu",	"Danaba",	"Godoma",	"Wagberi",	"Township",	"Barwago",	"Khorof/Harar",	"Elben",	"Sarman",	"Tarbaj",	"Wargadud",	"Arbajahan",	"Hadado/Athibohol",	"Ademasajide",	"Wagalla/Ganyure",	"Eldas",	"Della",	"Lakoley South/Basir",	"Elnur/Tula Tula",	"Benane",	"Burder",	"Dadaja Bulla",	"Habasswein",	"Lagboghol South",	"Ibrahim Ure",	"Diif",	"Takaba South",	"Takaba",	"Lag Sure",	"Dandu",	"Gither",	"Banissa",	"Derkhale",	"Guba",	"Malkamari",	"Kiliwehiri",	"Ashabito",	"Guticha",	"Morothile",	"Rhamu",	"Rhamu-Dimtu",	"Wargudud",	"Kutulo",	"Elwak South",	"Elwak North",	"Shimbir Fatuma",	"Arabia",	"Bulla Mpya",	"Khalalio",	"Neboi",	"Township",	"Libehia",	"Fino",	"Lafey",	"Warankara",	"Alungo Gof",	"Butiye",	"Sololo",	"Heilu-Manyatta",	"Golbo",	"Moyale Township",	"Uran",	"Obbu",	"Illeret",	"North Horr",	"Dukana",	"Maikona",	"Turbi",	"Sagante/Jaldesa",	"Karare",	"Marsabit Central",	"Loiyangalani",	"Kargi/South Horr",	"Korr/Ngurunit",	"Log Logo",	"Laisamis",	"Wabera",	"Bulla Pesa",	"Chari",	"Cherab",	"Ngare Mara",	"Burat",	"Oldonyiro",	"Garbatulla",	"Kinna",	"Sericho",	"Maua",	"Kiegoi/Antubochiu",	"Athiru Gaiti",	"Akachiu",	"Kanuni",	"Akirang'Ondu",	"Athiru Ruujine",	"Igembe East",	"Njia",	"Kangeta",	"Antuambui",	"Ntunene",	"Antubetwe Kiongo",	"Naathu",	"Amwathi",	"Athwana",	"Akithii",	"Kianjai",	"Nkomo",	"Mbeu",	"Thangatha",	"Mikinduri",	"Kiguchwa",	"Muthara",	"Karama",	"Municipality",	"Ntima East",	"Ntima West",	"Nyaki West",	"Nyaki East",	"Timau",	"Kisima",	"Kiirua/Naari",	"Ruiri/Rwarera",	"Kibirichia",	"Mwanganthia",	"Abothuguchi Central",	"Abothuguchi West",	"Kiagu",	"Mitunguu",	"Igoji East",	"Igoji West",	"Abogeta East",	"Abogeta West",	"Nkuene",	"Mitheru",	"Muthambi",	"Mwimbi",	"Ganga",	"Chogoria",	"Mariani",	"Karingani",	"Magumoni",	"Mugwe",	"Igambang'Ombe",	"Gatunga",	"Mukothima",	"Nkondi",	"Chiakariga",	"Marimanti",	"Ruguru/Ngandori",	"Kithimu",	"Nginda",	"Mbeti North",	"Kirimari",	"Gaturi South",	"Gaturi North",	"Kagaari South",	"Central  Ward",	"Kagaari North",	"Kyeni North",	"Kyeni South",	"Mwea",	"Makima",	"Mbeti South",	"Mavuria",	"Kiambere",	"Nthawa",	"Muminji",	"Evurore",	"Ngomeni",	"Kyuso",	"Mumoni",	"Tseikuru",	"Tharaka",	"Kyome/Thaana",	"Nguutani",	"Migwani",	"Kiomo/Kyethani",	"Central",	"Kivou",	"Nguni",	"Nuu",	"Mui",	"Waita",	"Mutonguni",	"Kauwi",	"Matinyani",	"Kwa Mutonga/Kithumula",	"Kisasi",	"Mbitini",	"Kwavonza/Yatta",	"Kanyangi",	"Miambani",	"Township",	"Kyangwithya West",	"Mulango",	"Kyangwithya East",	"Zombe/Mwitika",	"Chuluni",	"Nzambani",	"Voo/Kyamatu",	"Endau/Malalani",	"Mutito/Kaliku",	"Ikanga/Kyatune",	"Mutomo",	"Mutha",	"Ikutha",	"Kanziko",	"Athi",	"Kivaa",	"Masinga Central",	"Ekalakala",	"Muthesya",	"Ndithini",	"Ndalani",	"Matuu",	"Kithimani",	"Ikombe",	"Katangi",	"Kangundo North",	"Kangundo Central",	"Kangundo East",	"Kangundo West",	"Tala",	"Matungulu North",	"Matungulu East",	"Matungulu West",	"Kyeleni",	"Mitaboni",	"Kathiani Central",	"Upper Kaewa/Iveti",	"Lower Kaewa/Kaani",	"Athi River",	"Kinanie",	"Muthwani",	"Syokimau/Mulolongo",	"Kalama",	"Mua",	"Mutituni",	"Machakos Central",	"Mumbuni North",	"Muvuti/Kiima-Kimwe",	"Kola",	"Mbiuni",	"Makutano/ Mwala",	"Masii",	"Muthetheni",	"Wamunyu",	"Kibauni",	"Tulimani",	"Mbooni",	"Kithungo/Kitundu",	"Kisau/Kiteta",	"Waia/Kako",	"Kalawa",	"Kasikeu",	"Mukaa",	"Kiima Kiu/Kalanzoni",	"Ukia",	"Kee",	"Kilungu",	"Ilima",	"Wote",	"Muvau/Kikuumini",	"Mavindini",	"Kitise/Kithuki",	"Kathonzweni",	"Nzaui/Kilili/Kalamba",	"Mbitini",	"Makindu",	"Nguumo",	"Kikumbulyu North",	"Kikumbulyu South",	"Nguu/Masumba",	"Emali/Mulala",	"Masongaleni",	"Mtito Andei",	"Thange",	"Ivingoni/Nzambani",	"Engineer",	"Gathara",	"North Kinangop",	"Murungaru",	"Njabini/Kiburu",	"Nyakio",	"Githabai",	"Magumu",	"Wanjohi",	"Kipipiri",	"Geta",	"Githioro",	"Karau",	"Kanjuiri Ridge",	"Mirangine",	"Kaimbaga",	"Rurii",	"Gathanji",	"Gatimu",	"Weru",	"Charagita",	"Leshau Pondo",	"Kiriita",	"Central",	"Shamata",	"Dedan Kimanthi",	"Wamagana",	"Aguthi/Gaaki",	"Mweiga",	"Naromoru Kiamathaga",	"Mwiyogo/Endarasha",	"Mugunda",	"Gatarakwa",	"Thegu River",	"Kabaru",	"Gakawa",	"Ruguru",	"Magutu",	"Iriaini",	"Konyu",	"Kirimukuyu",	"Karatina Town",	"Mahiga",	"Iria-Ini",	"Chinga",	"Karima",	"Gikondi",	"Rugi",	"Mukurwe-Ini West",	"Mukurwe-Ini Central",	"Kiganjo/Mathari",	"Rware",	"Gatitu/Muruguru",	"Ruring'U",	"Kamakwa/Mukaro",	"Mutithi",	"Kangai",	"Thiba",	"Wamumu",	"Nyangati",	"Murinduko",	"Gathigiriri",	"Tebere",	"Kabare",	"Baragwi",	"Njukiini",	"Ngariama",	"Karumandi",	"Mukure",	"Kiine",	"Kariti",	"Mutira",	"Kanyeki-Ini",	"Kerugoya",	"Inoi",	"Kanyenyaini",	"Muguru",	"Rwathia",	"Gitugi",	"Kiru",	"Kamacharia",	"Wangu",	"Mugoiri",	"Mbiri",	"Township",	"Murarandia",	"Gaturi",	"Kahumbu",	"Muthithi",	"Kigumo",	"Kangari",	"Kinyona",	"Kimorori/Wempa",	"Makuyu",	"Kambiti",	"Kamahuha",	"Ichagaki",	"Nginda",	"Ng'Araria",	"Muruka",	"Kagundu-Ini",	"Gaichanjiru",	"Ithiru",	"Ruchu",	"Ithanga",	"Kakuzi/Mitubiri",	"Mugumo-Ini",	"Kihumbu-Ini",	"Gatanga",	"Kariara",	"Kiamwangi",	"Kiganjo",	"Ndarugu",	"Ngenda",	"Gituamba",	"Githobokoni",	"Chania",	"Mang'U",	"Murera",	"Theta",	"Juja",	"Witeithie",	"Kalimoni",	"Township",	"Kamenu",	"Hospital",	"Gatuanyaga",	"Ngoliba",	"Gitothua",	"Biashara",	"Gatongora",	"Kahawa Sukari",	"Kahawa Wendani",	"Kiuu",	"Mwiki",	"Mwihoko",	"Githunguri",	"Githiga",	"Ikinu",	"Ngewa",	"Komothai",	"Ting'Ang'A",	"Ndumberi",	"Riabai",	"Township",	"Cianda",	"Karuri",	"Ndenderu",	"Muchatha",	"Kihara",	"Gitaru",	"Muguga",	"Nyadhuna",	"Kabete",	"Uthiru",	"Karai",	"Nachu",	"Sigona",	"Kikuyu",	"Kinoo",	"Bibirioni",	"Limuru Central",	"Ndeiya",	"Limuru East",	"Ngecha Tigoni",	"Kinale",	"Kijabe",	"Nyanduma",	"Kamburu",	"Lari/Kirenga",	"Kaeris",	"Lake Zone",	"Lapur",	"Kaaleng/Kaikor",	"Kibish",	"Nakalale",	"Kakuma",	"Lopur",	"Letea",	"Songot",	"Kalobeyei",	"Lokichoggio",	"Nanaam",	"Kerio Delta",	"Kang'Atotha",	"Kalokol",	"Lodwar Township",	"Kanamkemer",	"Kotaruk/Lobei",	"Turkwel",	"Loima",	"Lokiriama/Lorengippi",	"Kaputir",	"Katilu",	"Lobokat",	"Kalapata",	"Lokichar",	"Kapedo/Napeitom",	"Katilia",	"Lokori/Kochodin",	"Riwo",	"Kapenguria",	"Mnagei",	"Siyoi",	"Endugh",	"Sook",	"Sekerr",	"Masool",	"Lomut",	"Weiwei",	"Suam",	"Kodich",	"Kapckok",	"Kasei",	"Kiwawa",	"Alale",	"Chepareria",	"Batei",	"Lelan",	"Tapach",	"Lodokejek",	"Suguta Marmar",	"Maralal",	"Loosuk",	"Poro",	"El-Barta",	"Nachola",	"Ndoto",	"Nyiro",	"Angata Nanyokie",	"Baawa",	"Waso",	"Wamba West",	"Wamba East",	"Wamba North",	"Kapomboi",	"Kwanza",	"Keiyo",	"Bidii",	"Chepchoina",	"Endebess",	"Matumbei",	"Kinyoro",	"Matisi",	"Tuwani",	"Saboti",	"Machewa",	"Kiminini",	"Waitaluk",	"Sirende",	"Hospital",	"Sikhendu",	"Nabiswa",	"Sinyerere",	"Makutano",	"Kaplamai",	"Motosiet",	"Cherangany/Suwerwa",	"Chepsiro/Kiptoror",	"Sitatunga",	"Moi'S Bridge",	"Kapkures",	"Ziwa",	"Segero/Barsombe",	"Kipsomba",	"Soy",	"Kuinet/Kapsuswa",	"Ngenyilel",	"Tapsagoi",	"Kamagut",	"Kiplombe",	"Kapsaos",	"Huruma",	"Tembelio",	"Sergoit",	"Karuna/Meibeki",	"Moiben",	"Kimumu",	"Kapsoya",	"Kaptagat",	"Ainabkoi/Olare",	"Simat/Kapseret",	"Kipkenyo",	"Ngeria",	"Megun",	"Langas",	"Racecourse",	"Cheptiret/Kipchamo",	"Tulwet/Chuiyat",	"Tarakwa",	"Kapyego",	"Sambirir",	"Endo",	"Embobut / Embulot",	"Lelan",	"Sengwer",	"Cherang'Any/Chebororwa",	"Moiben/Kuserwo",	"Kapsowar",	"Arror",	"Emsoo",	"Kamariny",	"Kapchemutwa",	"Tambach",	"Kaptarakwa",	"Chepkorio",	"Soy North",	"Soy South",	"Kabiemit",	"Metkei",	"Songhor/Soba",	"Tindiret",	"Chemelil/Chemase",	"Kapsimotwo",	"Kabwareng",	"Terik",	"Kemeloi-Maraba",	"Kobujoi",	"Kaptumo-Kaboi",	"Koyo-Ndurio",	"Nandi Hills",	"Chepkunyuk",	"Ol'Lessos",	"Kapchorua",	"Chemundu/Kapng'Etuny",	"Kosirai",	"Lelmokwo/Ngechek",	"Kaptel/Kamoiywo",	"Kiptuya",	"Chepkumia",	"Kapkangani",	"Kapsabet",	"Kilibwoni",	"Chepterwai",	"Kipkaren",	"Kurgung/Surungai",	"Kabiyet",	"Ndalat",	"Kabisaga",	"Sangalo/Kebulonik",	"Tirioko",	"Kolowa",	"Ribkwo",	"Silale",	"Loiyamorock",	"Tangulbei/Korossi",	"Churo/Amaya",	"Barwessa",	"Kabartonjo",	"Saimo/Kipsaraman",	"Saimo/Soi",	"Bartabwa",	"Kabarnet",	"Sacho",	"Tenges",	"Ewalel Chapchap",	"Kapropita",	"Marigat",	"Ilchamus",	"Mochongoi",	"Mukutani",	"Mogotio",	"Emining",	"Kisanana",	"Lembus",	"Lembus Kwen",	"Ravine",	"Mumberes/Maji Mazuri",	"Lembus/Perkerra",	"Koibatek",	"Olmoran",	"Rumuruti Township",	"Kinamba",	"Marmanet",	"Igwamiti",	"Salama",	"Ngobit",	"Tigithi",	"Thingithu",	"Nanyuki",	"Umande",	"Sosian",	"Segera",	"Mukogondo West",	"Mukogondo East",	"Mariashoni",	"Elburgon",	"Turi",	"Molo",	"Maunarok",	"Mauche",	"Kihingo",	"Nessuit",	"Lare",	"Njoro",	"Biashara",	"Hells Gate",	"Lakeview",	"Maai-Mahiu",	"Maiella",	"Olkaria",	"Naivasha East",	"Viwandani",	"Gilgil",	"Elementaita",	"Mbaruk/Eburu",	"Malewa West",	"Murindati",	"Amalo",	"Keringet",	"Kiptagich",	"Tinet",	"Kiptororo",	"Nyota",	"Sirikwa",	"Kamara",	"Subukia",	"Waseges",	"Kabazi",	"Menengai West",	"Soin",	"Visoi",	"Mosop",	"Solai",	"Dundori",	"Kabatini",	"Kiamaina",	"Lanet/Umoja",	"Bahati",	"Barut",	"London",	"Kaptembwo",	"Kapkures",	"Rhoda",	"Shaabab",	"Biashara",	"Kivumbini",	"Flamingo",	"Menengai",	"Nakuru East",	"Kilgoris Central",	"Keyian",	"Angata Barikoi",	"Shankoe",	"Kimintet",	"Lolgorian",	"Ilkerin",	"Ololmasani",	"Mogondo",	"Kapsasian",	"Olpusimoru",	"Olokurto",	"Narok Town",	"Nkareta",	"Olorropil",	"Melili",	"Mosiro",	"Ildamat",	"Keekonyokie",	"Suswa",	"Majimoto/Naroosura",	"Ololulung'A",	"Melelo",	"Loita",	"Sogoo",	"Sagamian",	"Ilmotiok",	"Mara",	"Siana",	"Naikarra",	"Olkeri",	"Ongata Rongai",	"Nkaimurunya",	"Oloolua",	"Ngong",	"Purko",	"Ildamat",	"Dalalekutuk",	"Matapato North",	"Matapato South",	"Kaputiei North",	"Kitengela",	"Oloosirkon/Sholinke",	"Kenyawa-Poka",	"Imaroro",	"Keekonyokie",	"Iloodokilani",	"Magadi",	"Ewuaso Oonkidong'I",	"Mosiro",	"Entonet/Lenkisim",	"Mbirikani/Eselenkei",	"Kuku",	"Rombo",	"Kimana",	"Londiani",	"Kedowa/Kimugul",	"Chepseon",	"Tendeno/Sorget",	"Kunyak",	"Kamasian",	"Kipkelion",	"Chilchila",	"Kapsoit",	"Ainamoi",	"Kapkugerwet",	"Kipchebor",	"Kipchimchim",	"Kapsaos",	"Kisiara",	"Tebesonik",	"Cheboin",	"Chemosot",	"Litein",	"Cheplanget",	"Kapkatet",	"Waldai",	"Kabianga",	"Cheptororiet/Seretut",	"Chaik",	"Kapsuser",	"Sigowet",	"Kaplelartet",	"Soliat",	"Soin",	"Ndanai/Abosi",	"Chemagel",	"Kipsonoi",	"Kapletundo",	"Rongena/Manaret",	"Kong'Asis",	"Nyangores",	"Sigor",	"Chebunyo",	"Siongiroi",	"Merigi",	"Kembu",	"Longisa",	"Kipreres",	"Chemaner",	"Silibwet Township",	"Ndaraweta",	"Singorwet",	"Chesoen",	"Mutarakwa",	"Chepchabas",	"Kimulot",	"Mogogosiek",	"Boito",	"Embomos",	"Mautuma",	"Lugari",	"Lumakanda",	"Chekalini",	"Chevaywa",	"Lwandeti",	"Likuyani",	"Sango",	"Kongoni",	"Nzoia",	"Sinoko",	"West Kabras",	"Chemuche",	"East Kabras",	"Butali/Chegulo",	"Manda-Shivanga",	"Shirugu-Mugai",	"South Kabras",	"Butsotso East",	"Butsotso South",	"Butsotso Central",	"Sheywe",	"Mahiakalo",	"Shirere",	"Ingostse-Mathia",	"Shinoyi-Shikomari-",	"Bunyala West",	"Bunyala East",	"Bunyala Central",	"Mumias Central",	"Mumias North",	"Etenje",	"Musanda",	"Lubinu/Lusheya",	"Isongo/Makunga/Malaha",	"East Wanga",	"Koyonzo",	"Kholera",	"Khalaba",	"Mayoni",	"Namamali",	"Marama West",	"Marama Central",	"Marenyo - Shianda",	"Marama North",	"Marama South",	"Kisa North",	"Kisa East",	"Kisa West",	"Kisa Central",	"Isukha North",	"Murhanda",	"Isukha Central",	"Isukha South",	"Isukha East",	"Isukha West",	"Idakho South",	"Idakho East",	"Idakho North",	"Idakho Central",	"Lugaga-Wamuluma",	"South Maragoli",	"Central Maragoli",	"Mungoma",	"Lyaduywa/Izava",	"West Sabatia",	"Chavakali",	"North Maragoli",	"Wodanga",	"Busali",	"Shiru",	"Muhudu",	"Shamakhokho",	"Gisambai",	"Banja",	"Tambua",	"Jepkoyai",	"Luanda Township",	"Wemilabi",	"Mwibona",	"Luanda South",	"Emabungo",	"North East Bunyore",	"Central Bunyore",	"West Bunyore",	"Cheptais",	"Chesikaki",	"Chepyuk",	"Kapkateny",	"Kaptama",	"Elgon",	"Namwela",	"Malakisi/South Kulisiru",	"Lwandanyi",	"Kabuchai/Chwele",	"West Nalondo",	"Bwake/Luuya",	"Mukuyuni",	"South Bukusu",	"Bumula",	"Khasoko",	"Kabula",	"Kimaeti",	"West Bukusu",	"Siboti",	"Bukembe West",	"Bukembe East",	"Township",	"Khalaba",	"Musikoma",	"East Sang'Alo",	"Marakaru/Tuuti",	"Sang'Alo West",	"Mihuu",	"Ndivisi",	"Maraka",	"Misikhu",	"Sitikho",	"Matulo",	"Bokoli",	"Kimilili",	"Kibingei",	"Maeni",	"Kamukuywa",	"Mbakalo",	"Naitiri/Kabuyefwe",	"Milima",	"Ndalu/ Tabani",	"Tongaren",	"Soysambu/ Mitua",	"Malaba Central",	"Malaba North",	"Ang'Urai South",	"Ang'Urai North",	"Ang'Urai East",	"Malaba South",	"Ang'Orom",	"Chakol South",	"Chakol North",	"Amukura West",	"Amukura East",	"Amukura Central",	"Nambale Township",	"Bukhayo North/Waltsi",	"Bukhayo East",	"Bukhayo Central",	"Bukhayo West",	"Mayenje",	"Matayos South",	"Busibwabo",	"Burumba",	"Marachi West",	"Kingandole",	"Marachi Central",	"Marachi East",	"Marachi North",	"Elugulu",	"Namboboto Nambuku",	"Nangina",	"Ageng'A Nanguba",	"Bwiri",	"Bunyala Central",	"Bunyala North",	"Bunyala West",	"Bunyala South",	"West Ugenya",	"Ukwala",	"North Ugenya",	"East Ugenya",	"Sidindi",	"Sigomere",	"Ugunja",	"Usonga",	"West Alego",	"Central Alego",	"Siaya Township",	"North Alego",	"South East Alego",	"North Gem",	"West Gem",	"Central Gem",	"Yala Township",	"East Gem",	"South Gem",	"West Yimbo",	"Central Sakwa",	"South Sakwa",	"Yimbo East",	"West Sakwa",	"North Sakwa",	"East Asembo",	"West Asembo",	"North Uyoma",	"South Uyoma",	"West Uyoma",	"Kajulu",	"Kolwa East",	"Manyatta 'B'",	"Nyalenda 'A'",	"Kolwa Central",	"South West Kisumu",	"Central Kisumu",	"Kisumu North",	"West Kisumu",	"North West Kisumu",	"Railways",	"Migosi",	"Shaurimoyo Kaloleni",	"Market Milimani",	"Kondele",	"Nyalenda B",	"West Seme",	"Central Seme",	"East Seme",	"North Seme",	"East Kano/Wawidhi",	"Awasi/Onjiko",	"Ahero",	"Kabonyo/Kanyagwal",	"Kobura",	"Miwani",	"Ombeyi",	"Masogo/Nyang'Oma",	"Chemelil",	"Muhoroni/Koru",	"South West Nyakach",	"North Nyakach",	"Central Nyakach",	"West Nyakach",	"South East Nyakach",	"West Kasipul",	"South Kasipul",	"Central Kasipul",	"East Kamagak",	"West Kamagak",	"Kabondo East",	"Kabondo West",	"Kokwanyo/Kakelo",	"Kojwach",	"West Karachuonyo",	"North Karachuonyo",	"Central",	"Kanyaluo",	"Kibiri",	"Wangchieng",	"Kendu Bay Town",	"West Gem",	"East Gem",	"Kagan",	"Kochia",	"Homa Bay Central",	"Homa Bay Arujo",	"Homa Bay West",	"Homa Bay East",	"Kwabwai",	"Kanyadoto",	"Kanyikela",	"North Kabuoch",	"Kabuoch South/Pala",	"Kanyamwa Kologi",	"Kanyamwa Kosewe",	"Mfangano Island",	"Rusinga Island",	"Kasgunga",	"Gembe",	"Lambwe",	"Gwassi South",	"Gwassi North",	"Kaksingri West",	"Ruma Kaksingri East",	"North Kamagambo",	"Central Kamagambo",	"East Kamagambo",	"South Kamagambo",	"North Sakwa",	"South Sakwa",	"West Sakwa",	"Central Sakwa",	"God Jope",	"Suna Central",	"Kakrao",	"Kwa",	"Wiga",	"Wasweta Ii",	"Ragana-Oruba",	"Wasimbete",	"West Kanyamkago",	"North Kanyamkago",	"Central Kanyamkago",	"South Kanyamkago",	"East Kanyamkago",	"Kachien'G",	"Kanyasa",	"North Kadem",	"Macalder/Kanyarwanda",	"Kaler",	"Got Kachola",	"Muhuru",	"Bukira East",	"Bukira Centrl/Ikerege",	"Isibania",	"Makerero",	"Masaba",	"Tagare",	"Nyamosense/Komosoko",	"Gokeharaka/Getambwega",	"Ntimaru West",	"Ntimaru East",	"Nyabasi East",	"Nyabasi West",	"Bomariba",	"Bogiakumu",	"Bomorenda",	"Riana",	"Tabaka",	"Boikang'A",	"Bogetenga",	"Borabu / Chitago",	"Moticho",	"Getenga",	"Bombaba Borabu",	"Boochi Borabu",	"Bokimonge",	"Magenche",	"Masige West",	"Masige East",	"Bobasi Central",	"Nyacheki",	"Bobasi Bogetaorio",	"Bobasi Chache",	"Sameta/Mokwerero",	"Bobasi Boitangare",	"Majoge",	"Boochi/Tendere",	"Bosoti/Sengera",	"Ichuni",	"Nyamasibi",	"Masimba",	"Gesusu",	"Kiamokama",	"Bobaracho",	"Kisii Central",	"Keumbu",	"Kiogoro",	"Birongo",	"Ibeno",	"Monyerero",	"Sensi",	"Marani",	"Kegogi",	"Bogusero",	"Bogeka",	"Nyakoe",	"Kitutu   Central",	"Nyatieko",	"Rigoma",	"Gachuba",	"Kemera",	"Magombo",	"Manga",	"Gesima",	"Nyamaiya",	"Bogichora",	"Bosamaro",	"Bonyamatuta",	"Township",	"Itibo",	"Bomwagamo",	"Bokeira",	"Magwagwa",	"Ekerenyo",	"Mekenene",	"Kiabonyoru",	"Nyansiongo",	"Esise",	"Kitisuru",	"Parklands/Highridge",	"Karura",	"Kangemi",	"Mountain View",	"Kilimani",	"Kawangware",	"Gatina",	"Kileleshwa",	"Kabiro",	"Mutuini",	"Ngando",	"Riruta",	"Uthiru/Ruthimitu",	"Waithaka",	"Karen",	"Nairobi West",	"Mugumo-Ini",	"South-C",	"Nyayo Highrise",	"Laini Saba",	"Lindi",	"Makina",	"Woodley/Kenyatta Golf",	"Sarangombe",	"Githurai",	"Kahawa West",	"Zimmerman",	"Roysambu",	"Kahawa",	"Claycity",	"Mwiki",	"Kasarani",	"Njiru",	"Ruai",	"Baba Dogo",	"Utalii",	"Mathare North",	"Lucky Summer",	"Korogocho",	"Imara Daima",	"Kwa Njenga",	"Kwa Reuben",	"Pipeline",	"Kware",	"Kariobangi North",	"Dandora Area I",	"Dandora Area Ii",	"Dandora Area Iii",	"Dandora Area Iv",	"Kayole North",	"Kayole Central",	"Kayole South",	"Komarock",	"Matopeni",	"Upper Savannah",	"Lower Savannah",	"Embakasi",	"Utawala",	"Mihango",	"Umoja I",	"Umoja Ii",	"Mowlem",	"Kariobangi South",	"Makongeni",	"Maringo/Hamza",	"Harambee",	"Viwandani",	"Pumwani",	"Eastleigh North",	"Eastleigh South",	"Airbase",	"California",	"Nairobi Central",	"Ngara",	"Ziwani/Kariokor",	"Pangani",	"Landimawe",	"Nairobi South",	"Hospital",	"Mabatini",	"Huruma",	"Ngei",	"Mlango Kubwa",	"Kiamaiko",

    };
    private DatabaseReference surveyRef,mDatabaseUsers,countyRef,subCountyRef,wardRef;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private AutoCompleteTextView actvCounty,actvSubCounty,actvWards;
    private TextView villageName;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);
        villageName = findViewById(R.id.village_name);
        village =getIntent().getExtras().getString("Village");
        villageName.setText(village);

        post_key = getIntent().getExtras().getString("PostKey");

        sources = getIntent().getExtras().getString("Sources");
        fresh = getIntent().getExtras().getString("Fresh");
        salty = getIntent().getExtras().getString("Salty");
     //   sourceType =getIntent().getExtras().getString("sourceType");


        layout = findViewById(R.id.display);

        mAuth = FirebaseAuth.getInstance();

      //  OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadRiverSubtotals.class )

        //        .build();

        ArrayAdapter<String> countyAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,counties);
        ArrayAdapter<String> subCountyAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,subcounties);
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,wards);


        actvCounty = findViewById(R.id.countyAutoComplete);
        actvCounty.setThreshold(1);
        actvCounty.setAdapter(countyAdapter);
        actvCounty.setTextColor(getResources().getColor(R.color.black));

        actvSubCounty =findViewById(R.id.subCountyAutoComplete);
        actvSubCounty.setThreshold(1);
        actvSubCounty.setAdapter(subCountyAdapter);
        actvSubCounty.setTextColor(getResources().getColor(R.color.black));

        actvWards =findViewById(R.id.wardAutoComplete);
        actvWards.setThreshold(1);
        actvWards.setAdapter(wardAdapter);
        actvWards.setTextColor(getResources().getColor(R.color.black));

        progressBar = new ProgressBar(StepTwoRiversActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(StepTwoRiversActivity.this, LoginActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Survey").child(post_key);
        countyRef =FirebaseDatabase.getInstance().getReference().child("Counties");
        subCountyRef =FirebaseDatabase.getInstance().getReference().child("SubCounties");
        wardRef =FirebaseDatabase.getInstance().getReference().child("Wards");
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        //Initialize the instance of the firebase user
                mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user


    }
    public void validate() {
        boolean valid = true;
        //final String mLatitude = latitude;
        //final String mLongitude = longitude;
        //String sources = getIntent().getExtras().getString("Sources");
        //String fresh = fresh;
        //String salty = numberSalty.getText().toString();
        //String village = nameOfVillage.getText().toString();




        String county = actvCounty.getText().toString();
        String subCounty = actvSubCounty.getText().toString();
        String ward = actvWards.getText().toString();

        final String sourceType = "Rivers";


        if (county.isEmpty()) {
            actvCounty.setError("Required");
            valid = false;
        } else {
            actvCounty.setError(null);
        }
        if (subCounty.isEmpty()) {
            actvSubCounty.setError("Required");
            valid = false;
        } else {
            actvSubCounty.setError(null);
        }
        if (ward.isEmpty()) {
            actvWards.setError("Required");
            valid = false;
        } else {
            actvWards.setError(null);
        }
        if (valid) {

            UploadRiverSubtotals.setRiverData(sources,fresh,salty,county,subCounty,ward);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            surveyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //add the profilePhoto and displayName for the current user

                    surveyRef.child("county").setValue(county );
                    surveyRef.child("subCounty").setValue(subCounty);
                    surveyRef.child("ward").setValue(ward).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //show a toast to indicate the profile was updated

                                //launch the login activity
                                progressDialog.dismiss();
                                Toast.makeText(StepTwoRiversActivity.this, " Upload Successful", Toast.LENGTH_SHORT).show();
                                // Toast.makeText(StepTwoRiversActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                                final String post_key = surveyRef.getKey();

                                progressBar.setVisibility(View.GONE);
                                scheduleJob();
                                Intent next = new Intent(StepTwoRiversActivity.this, MainActivity.class);
                                //next.putExtra("PostKey", post_key);
                               // next.putExtra("sourceType",sourceType);

                                startActivity(next);
                                finish();
                            }
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

/*
            final  DatabaseReference newCounty = countyRef.child(county).push();

                                            countyRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    // countyRef.child("myCounties").setValue(county);
                                                    newCounty.child("subcounty").setValue(subCounty);
                                                    newCounty.child("village").setValue(village + " " + "Village");
                                                    newCounty.child("sourceTotal").setValue(sources);
                                                    newCounty.child("sourcesFresh").setValue(fresh);
                                                    newCounty.child("sourcesSalty").setValue(salty);
                                                    newCounty.child("totalRivers").setValue(sources);
                                                    newCounty.child("riversFresh").setValue(fresh);
                                                    newCounty.child("riversSalty").setValue(salty);
                                                    newCounty.child("totalLakes").setValue("0");
                                                    newCounty.child("lakesFresh").setValue("0");
                                                    newCounty.child("lakesSalty").setValue("0");
                                                    newCounty.child("totalSprings").setValue("0");
                                                    newCounty.child("springsFresh").setValue("0");
                                                    newCounty.child("springSalty").setValue("0");
                                                    newCounty.child("totalDams").setValue("0");
                                                    newCounty.child("damsFresh").setValue("0");
                                                    newCounty.child("damsSalty").setValue("0");
                                                    newCounty.child("totalBoreholes").setValue("0");
                                                    newCounty.child("boreholesFresh").setValue("0");
                                                    newCounty.child("boreholesSalty").setValue("0");
                                                    newCounty.child("totalTaps").setValue("0");
                                                    newCounty.child("tapsFresh").setValue("0");
                                                    newCounty.child("tapsSalty").setValue("0");

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            final  DatabaseReference newSubCounty = subCountyRef.child(subCounty).push();

                                            subCountyRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    // countyRef.child("myCounties").setValue(county);
                                                    newSubCounty.child("subcounty").setValue(subCounty);
                                                    newSubCounty.child("village").setValue(village + " " + "Village");
                                                    newSubCounty.child("sourceTotal").setValue(sources);
                                                    newSubCounty.child("sourcesFresh").setValue(fresh);
                                                    newSubCounty.child("sourcesSalty").setValue(salty);
                                                    newSubCounty.child("totalRivers").setValue(sources);
                                                    newSubCounty.child("riversFresh").setValue(fresh);
                                                    newSubCounty.child("riversSalty").setValue(salty);
                                                    newSubCounty.child("totalLakes").setValue("0");
                                                    newSubCounty.child("lakesFresh").setValue("0");
                                                    newSubCounty.child("lakesSalty").setValue("0");
                                                    newSubCounty.child("totalSprings").setValue("0");
                                                    newSubCounty.child("springsFresh").setValue("0");
                                                    newSubCounty.child("springSalty").setValue("0");
                                                    newSubCounty.child("totalDams").setValue("0");
                                                    newSubCounty.child("damsFresh").setValue("0");
                                                    newSubCounty.child("damsSalty").setValue("0");
                                                    newSubCounty.child("totalBoreholes").setValue("0");
                                                    newSubCounty.child("boreholesFresh").setValue("0");
                                                    newSubCounty.child("boreholesSalty").setValue("0");
                                                    newSubCounty.child("totalTaps").setValue("0");
                                                    newSubCounty.child("tapsFresh").setValue("0");
                                                    newSubCounty.child("tapsSalty").setValue("0");

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            final  DatabaseReference newWard = wardRef.child(ward).push();

                                            wardRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    // countyRef.child("myCounties").setValue(county);
                                                    newWard.child("subcounty").setValue(subCounty);
                                                    newWard.child("village").setValue(village + " " + "Village");
                                                    newWard.child("sourceTotal").setValue(sources);
                                                    newWard.child("sourcesFresh").setValue(fresh);
                                                    newWard.child("sourcesSalty").setValue(salty);
                                                    newWard.child("totalRivers").setValue(sources);
                                                    newWard.child("riversFresh").setValue(fresh);
                                                    newWard.child("riversSalty").setValue(salty);
                                                    newWard.child("totalLakes").setValue("0");
                                                    newWard.child("lakesFresh").setValue("0");
                                                    newWard.child("lakesSalty").setValue("0");
                                                    newWard.child("totalSprings").setValue("0");
                                                    newWard.child("springsFresh").setValue("0");
                                                    newWard.child("springSalty").setValue("0");
                                                    newWard.child("totalDams").setValue("0");
                                                    newWard.child("damsFresh").setValue("0");
                                                    newWard.child("damsSalty").setValue("0");
                                                    newWard.child("totalBoreholes").setValue("0");
                                                    newWard.child("boreholesFresh").setValue("0");
                                                    newWard.child("boreholesSalty").setValue("0");
                                                    newWard.child("totalTaps").setValue("0");
                                                    newWard.child("tapsFresh").setValue("0");
                                                    newWard.child("tapsSalty").setValue("0")
                                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //show a toast to indicate the profile was updated
                                                                 Toast.makeText(StepTwoRiversActivity.this, " Upload Successful", Toast.LENGTH_SHORT).show();
                                                                //launch the login activity
                                                                progressDialog.dismiss();
                                                                // Toast.makeText(StepTwoRiversActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                                                                final String post_key = surveyRef.getKey();

                                                                progressBar.setVisibility(View.GONE);
                                                                Intent next = new Intent(StepTwoRiversActivity.this, VideoActivity.class);
                                                                next.putExtra("PostKey", post_key);

                                                                startActivity(next);
                                                                finish();
                                                            }
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });




                                           // surveyRef.push();


                                            //call the method addValueEventListener to publish the additions in  the database reference of a specific user


 */
                                        }

    }
    @Override
    public void onStart() {
        super.onStart();



        FirebaseUser currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ1);
        if (currentUser  !=null) {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    validate();

                }
            });

        }
    }
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(UploadRiverSubtotals.class)
                .build();
        Date videDate = new Date();

        SimpleDateFormat dateFor = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

        String postRandomName = dateFor.format(videDate);

        WorkManager.getInstance(this).enqueueUniqueWork(postRandomName, ExistingWorkPolicy.KEEP,work);
     //   WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }



}
