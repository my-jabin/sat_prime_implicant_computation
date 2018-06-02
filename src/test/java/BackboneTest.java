import masterthesis.base.ApplicationContext;
import masterthesis.base.Implicant;
import masterthesis.base.Literal;
import masterthesis.base.Problem;
import masterthesis.utils.Debug;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackboneTest {

    private static final String SATPATH = "src/main/resources/sat/";
    private static final String UNSATPATH = "src/main/resources/unsat/";
    private static final String BACKBONEPATH = "src/main/resources/backbone/";
    private ApplicationContext ac;

    @Before
    public void init() {
        this.ac = ApplicationContext.getInstance();
    }

    @Test
    public void testBackbone2WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula02.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(5, backbone.size());
        // time taken: 287ms
    }

    @Test
    public void testBackboneWithChunks02() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula02.cnf";
        Set<Literal> backbone = Problem.backboneWithChunks(fileNameTest, 1);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(5, backbone.size());
        // time taken: 382ms
    }

    @Test
    public void testBackbone3WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula03.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(3, backbone.size());
        // 357ms
    }

    @Test
    public void testBackboneWithChunks03() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula03.cnf";
        Set<Literal> backbone = Problem.backboneWithChunks(fileNameTest, 1);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(3, backbone.size());
        // time taken: 307ms
    }


    @Test
    public void testBackbone4() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula04.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
        ArrayList<Literal> backbone = Problem.backbones(allPI);

        Assert.assertEquals(4, backbone.size());
        Assert.assertTrue(backbone.contains(ac.getLiteral(-6)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(-3)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(-1)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(-2)));
    }


    @Test
    public void testBackbone4WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula04.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(4, backbone.size());
        Assert.assertTrue(backbone.contains(ac.getLiteral(-6)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(-3)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(-1)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(-2)));
    }


    @Test
    public void testBackboneWithPrimeInterImprove4() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula04.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInterImprov(fileNameTest);
        Debug.printPrettyString(true, backbone);
    }

    @Test
    public void testBackbone5() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
        ArrayList<Literal> backbone = Problem.backbones(allPI);
        Debug.printPrettyString(true, backbone);

        Assert.assertEquals(4, backbone.size());
        Assert.assertTrue(backbone.contains(ac.getLiteral(1)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(2)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(3)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(4)));
    }

    @Test
    public void testBackbone5WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";

        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(4, backbone.size());
        Assert.assertTrue(backbone.contains(ac.getLiteral(1)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(2)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(3)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(4)));
    }

    @Test
    public void testBackbone6() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula06.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
        ArrayList<Literal> backbone = Problem.backbones(allPI);
        Debug.printPrettyString(true, backbone);

        // times taken: 30s 5ms
        Assert.assertEquals(0, backbone.size());
    }

    @Test
    public void testBackbone6WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula06.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        // times taken: 654ms
        Assert.assertEquals(0, backbone.size());
    }

    @Test
    public void testBackbone6WithPrimeInterImprov() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula06.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInterImprov(fileNameTest);
        Debug.printPrettyString(true, backbone);
        // times taken: 365ms
        Assert.assertEquals(0, backbone.size());
    }

    @Test
    public void testBackboneWithChunks06() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula06.cnf";
        Set<Literal> backbone = Problem.backboneWithChunks(fileNameTest, 1);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(0, backbone.size());
        // time taken: 400ms
    }

    @Test
    public void testBackbone7WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(0, backbone.size());
    }

    @Test
    public void testBackbone8WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula08.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(0, backbone.size());
    }

    @Test
    public void testBackbone9() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula09.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
        allPI.forEach(implicant -> System.out.println(implicant.toPrettyString()));
        ArrayList<Literal> backbone = Problem.backbones(allPI);

        Assert.assertEquals(2, backbone.size());
        Assert.assertTrue(backbone.contains(ac.getLiteral(1)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(3)));
    }

    @Test
    public void testBackbone9WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula09.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(2, backbone.size());
        Assert.assertTrue(backbone.contains(ac.getLiteral(1)));
        Assert.assertTrue(backbone.contains(ac.getLiteral(3)));
    }

    @Test
    public void testBackbone1() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "dp02s02.shuffled.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
//        allPI.forEach(implicant -> System.out.println(implicant.toPrettyString()));
        ArrayList<Literal> backbone = Problem.backbones(allPI);
        Assert.assertEquals(190, backbone.size());
        // time taken: 875ms
        // backbone size = 190
    }

    @Test
    public void testBackboneWithPrimeInter1() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "dp02s02.shuffled.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(190, backbone.size());
        // time taken: 778ms
        // backbone size = 190
    }

    @Test
    public void testBackboneWithPrimeInterImprove1() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "dp02s02.shuffled.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInterImprov(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(190, backbone.size());
        // time taken: 1s 929ms
        // backbone size = 190
    }

    @Test
    public void testBackboneWithKaiserKeuchlin() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "dp02s02.shuffled.cnf";
        Set<Literal> backbone = Problem.backboneWithKaiserKuechlin(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(190, backbone.size());
        // time taken: 2s 656ms
        // backbone size = 190
    }

    @Test
    public void testBackboneWithChunks() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "dp02s02.shuffled.cnf";
        Set<Literal> backbone = Problem.backboneWithChunks(fileNameTest, 10);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(190, backbone.size());
        // time taken: 812ms
        // backbone size = 190
    }


    @Test
    public void testBackbone10WithPrimeInter() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula10.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(200, backbone.size());
        // time taken: 516ms
    }

    @Test
    public void testBackbone10() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula10.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
        ArrayList<Literal> backbone = Problem.backbones(allPI);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(200, backbone.size());
        // time taken: 543ms
        // backbone size = 200
    }

    @Test
    public void testBackboneWithPrimInterImprove10() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula10.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInterImprov(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(200, backbone.size());
        // time taks: 3s
        // backbone size =200
    }


    @Test
    public void testBackbone10WithPrimeCover1() {
        this.ac.reset();
        final String fileNameTest = BACKBONEPATH + "b10/CBS_k3_n100_m449_b10_1.cnf";
        Set<Implicant> allPI = Problem.primeImplicantCover(fileNameTest);
        ArrayList<Literal> backbone = Problem.backbones(allPI);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(10, backbone.size());
        // time taken: 4m 39s 686ms
        // backbone(10): (8 11 12 -33 48 57 -62 72 92 97)
    }

    @Test
    public void testBackbone10WithPrimeInter1() {
        this.ac.reset();
        final String fileNameTest = BACKBONEPATH + "b10/CBS_k3_n100_m449_b10_1.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(10, backbone.size());
        // time taken: 1m 11s 554ms
        // backbone(10): (8 11 12 -33 48 57 -62 72 92 97)
    }


    @Test
    public void testBackbone10WithPrimeCoverImprov1() {
        this.ac.reset();
        final String fileNameTest = BACKBONEPATH + "b10/CBS_k3_n100_m449_b10_1.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInterImprov(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(10, backbone.size());
        // time taken: 1s 346ms
        // backbone(10): (8 11 12 -33 48 57 -62 72 92 97)
    }

    @Test
    public void testBackbone10WithChunks() {
        this.ac.reset();
        final String fileNameTest = BACKBONEPATH + "b10/CBS_k3_n100_m449_b10_1.cnf";
        Set<Literal> backbone = Problem.backboneWithChunks(fileNameTest, 3);
        Debug.printPrettyString(false, backbone);
        Assert.assertEquals(10, backbone.size());
        // fastest time taken: 903ms
        // backbone(10): (8 11 12 -33 48 57 -62 72 92 97)
    }

    @Test
    public void testBackboneWithb10() throws IOException {
        //testBackboneWithPrimeInterImprov(10);
        // time take: 2s 711ms

        //testBackboneWithChunks(10,1);
        // time take: 1s 807ms

        //testBackboneWithChunks(10,3);
        // time take: 1s 748ms

        //testBackboneWithChunks(10,5);
        // time take: 1s 806ms

        //testBackboneWithChunks(10,10);
        // time take: 1s 940ms
    }


    @Test
    public void testBackboneWithb30() throws IOException {
        testBackboneWithPrimeInterImprov(30);
        // time take: 2s 874ms
    }


    @Test
    public void testBackboneWithb50() throws IOException {
        //testBackboneWithPrimeInterImprov(50);
        // time take: 2s 707ms

        //testBackboneWithChunks(50,1);
        // time take: 2s 424ms

        //testBackboneWithChunks(50,5);
        // time take: 1s 568ms

        //testBackboneWithChunks(50,15);
        // time take: 1s 464ms

        //testBackboneWithChunks(50,25);
        // time take: 1s 244ms

        testBackboneWithChunks(50, 40);
        // time take: 1s 188ms
    }

    @Test
    public void testBackboneWithb70() throws IOException {
        testBackboneWithPrimeInterImprov(70);
        // time take: 2s 973ms
    }

    @Test
    public void testBackboneWithb90() throws IOException {
        //testBackboneWithPrimeInterImprov(90);
        // time take: 2s 990ms

        //testBackboneWithChunks(90,1);
        // time take: 2s 955ms

        //testBackboneWithChunks(90,3);
        // time take: 2s 199ms

        //testBackboneWithChunks(90,10);
        // time take: 1s 532ms

        //testBackboneWithChunks(90,20);
        // time take: 1s 260ms

        //testBackboneWithChunks(90,40);
        // time take: 1s 75ms

        testBackboneWithChunks(90, 60);
        // time take: 1s 126ms
    }

    public void testBackboneWithPrimeInterImprov(int number) throws IOException {
        List<String> fileNames;
        Path start = Paths.get(BACKBONEPATH + "b" + number + "/");
        int maxDepth = 2;
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            fileNames = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(".cnf"))
                    .collect(Collectors.toList());
        }

        for (String name : fileNames) {
            this.ac.reset();
            Set<Literal> backbone = Problem.backbonePrimeInterImprov(name);
            Assert.assertEquals(number, backbone.size());
        }
    }

    public void testBackboneWithChunks(int number, int chunks) throws IOException {
        List<String> fileNames;
        Path start = Paths.get(BACKBONEPATH + "b" + number + "/");
        int maxDepth = 2;
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            fileNames = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(".cnf"))
                    .collect(Collectors.toList());
        }

        for (String name : fileNames) {
            this.ac.reset();
            Set<Literal> backbone = Problem.backboneWithChunks(name, chunks);
            Assert.assertEquals(number, backbone.size());
        }
    }


    @Test
    public void testBackboneWithPrimeInter2() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "D1119_M23.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInter(fileNameTest);
        Debug.printPrettyString(true, backbone);
        // todo: how to quickly compute backbone with big cnf file
        // think about how to determine a required literal is a locally or globally
    }

    @Test
    public void testBackboneWithPrimInterImprove() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "D1119_M23.cnf";
        Set<Literal> backbone = Problem.backbonePrimeInterImprov(fileNameTest);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(1354, backbone.size());
        // time taken: 1s 775ms
    }

    @Test
    public void testBackboneWithPrimInterChunks() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "D1119_M23.cnf";
        Set<Literal> backbone = Problem.backboneWithChunks(fileNameTest, 50);
        Debug.printPrettyString(true, backbone);
        Assert.assertEquals(1354, backbone.size());
        // time taken: 1s 230ms
    }


}
