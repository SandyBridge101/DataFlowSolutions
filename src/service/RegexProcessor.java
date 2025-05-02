package service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidInputException;
import exceptions.InvalidRequestException;
import exceptions.MatchNotFoundException;
import filehandler.FileHandler;
import model.*;
import service.*;
import utils.LoggerUtil;

public class RegexProcessor {
    final private  DataManager dataManager = new DataManager();
    private static final Logger logger = LoggerUtil.getLogger(RegexProcessor.class);


    //search
    public void findMatches(String pattern, String input) throws MatchNotFoundException {
        logger.info("Extracting matches...");
        Pattern p = Pattern.compile( pattern , Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        int count = 0;

        while (m.find()) {
            //matches.add(new CustomDataObject(UUID.randomUUID(),m.group()));
            System.out.println(m.group());
            dataManager.addEntry(UUID.randomUUID(),m.group(),m.start(),m.end());
            count++;
        }
        if (count == 0) {
            logger.info("Match not found");
            throw new MatchNotFoundException("Match not found");

        }



    }

    //match
    public boolean checkMatch(String pattern, String input) {
        boolean matches = false;
        if (input.matches(pattern)) {
            matches = true;
        }
        return matches;
    }

    //replace
    public String replaceMatches(String pattern, String replacement, String input) throws InvalidInputException {
        logger.info("Replacing matches...");
        try{
            for (CustomDataObject customDataObject : dataManager.getDataObjects()) {
                if(customDataObject.getValue().matches(pattern)) {
                    dataManager.deleteEntry(customDataObject.getId());
                }
            }
            return input.replaceAll(pattern, replacement);
        }catch (Exception e) {
            logger.info("There is an issue processing this request.\n Check the parameters and try again.");
            throw new InvalidRequestException("There is an issue processing this request.\n Check the parameters and try again.");
        }
    }

    //replace at
    public String replaceAt(String original, int start, int end,UUID id, String replacement) throws InvalidInputException {
        try {
            dataManager.deleteEntry(id);
            StringBuilder sb = new StringBuilder(original);
            sb.replace(start, end, replacement != null ? replacement : "");
            for (CustomDataObject customDataObject : dataManager.getDataObjects()) {
                if(customDataObject.getStart() == start && customDataObject.getEnd() == end) {
                    dataManager.deleteEntry(customDataObject.getId());
                }
            }
            return sb.toString();
        }catch (Exception e) {
            logger.info("There is an issue processing this request.\n Check the parameters and try again.");
            throw new InvalidRequestException("There is an issue processing this request.\n Check the parameters and try again.");
        }
    }

    //delete
    public String deleteMatches(String pattern, String input) {
        try {
            for (CustomDataObject customDataObject : dataManager.getDataObjects()) {
                if(customDataObject.getValue().matches(pattern)) {
                    dataManager.deleteEntry(customDataObject.getId());
                }
            }
            return input.replaceAll(pattern, "");
        }catch (Exception e) {
            logger.info("There is an issue processing this request.\n Check the parameters and try again.");
            throw new InvalidRequestException("There is an issue processing this request.\n Check the parameters and try again.");
        }
    }

    //delete at
    public String deleteAt(String original, int start, int end, UUID id) {
        String replacement="";
        StringBuilder sb = new StringBuilder(original);
        try {
            sb.replace(start, end, replacement != null ? replacement : "");

            for (CustomDataObject customDataObject : dataManager.getDataObjects()) {
                if(customDataObject.getStart() == start && customDataObject.getEnd() == end) {
                    dataManager.deleteEntry(customDataObject.getId());
                }
            }

            dataManager.deleteEntry(id);
            return sb.toString();
        }catch (Exception e) {
            logger.info("There is an issue processing this request.\n Check the parameters and try again.");
            throw new InvalidRequestException("There is an issue processing this request.\n Check the parameters and try again.");

        }
    }



}