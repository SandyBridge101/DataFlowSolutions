package test;

import exceptions.*;
import model.*;
import org.junit.Before;
import org.junit.Test;
import service.DataManager;
import service.RegexProcessor;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;



public class UnitTests {
    private final RegexProcessor processor=new RegexProcessor();
    private final DataManager dataManager=new DataManager();
    private String inputText;

    @Before
    public void setUp() {
        dataManager.clearAllEntries();
        inputText="Despite the heavy rain and flickering streetlights, Jenna continued walking through the quiet town, clutching the worn notebook that held her grandfather’s last invention.\n" +
                " The pages, though water-stained, revealed sketches of a peculiar timekeeping device powered not by gears, but by magnetic pulses.\n" +
                "She wasn't entirely sure what it meant, but her gut told her it was important—important enough that someone had tried to steal it the night before.\n" +
                " Now, with only hours before sunrise, she had to find someone who could decipher the diagrams and possibly finish what her grandfather had started.\n";
    }

    @Test
    public void getWordMatchesTest() {
        String word="Jenna";
        processor.findMatches(word,inputText);
        List<CustomDataObject> matches=dataManager.getDataObjects();
        assertEquals(1, matches.size());

    }

    @Test
    public void getPatternMatchesTest() {
        String pattern="\\b[A-Za-z]{5}\\b";
        processor.findMatches(pattern,inputText);
        List<CustomDataObject> matches=dataManager.getDataObjects();
        assertEquals(12, matches.size());
    }

    @Test
    public void removeMatchTest() {
        String word="Jenna";
        processor.findMatches(word,inputText);
        List<CustomDataObject> matches=dataManager.getDataObjects();
        assertEquals(1, matches.size());
        UUID id=matches.getFirst().getId();
        processor.deleteMatches(word,inputText);
        assertNull(dataManager.getEntry(id));

    }

    @Test
    public void replaceMatchTest() {
        String word="Jenna";
        String newWord="Kai";
        String newInputText=processor.replaceMatches(word,newWord,inputText);
        processor.findMatches(newWord,newInputText);
        assertEquals(1,dataManager.getDataObjects().size());

    }

    @Test
    public void matchExceptionTest(){
        String word="Kofi";
        assertThrows(MatchNotFoundException.class,()->{processor.findMatches(word,inputText);});
    }





}
