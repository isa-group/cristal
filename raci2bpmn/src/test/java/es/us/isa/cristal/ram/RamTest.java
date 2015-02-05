package es.us.isa.cristal.ram;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.isa.cristal.ram.BoundedRole;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram.RamActivity;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * RamTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class RamTest {

    @Test
    public void loadsFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        RAM matrix = mapper.readValue(this.getClass().getResourceAsStream("completeRamMatrix.json"), RAM.class);

        Assert.assertEquals("number of activities", 2, matrix.getActivities().size());

        RamActivity activity = matrix.getActivityByName("Write Paper");
        Assert.assertNotNull("activity exists", activity);
        Assert.assertEquals("number of duties", 3, activity.getDutiesDefined().size());

        Assert.assertEquals("duties responsible", 1, activity.numberOf("responsible"));
        Assert.assertEquals("role", "PhDStudent", activity.getOne("responsible").getRole());

        Assert.assertEquals("duties responsible", 1, activity.numberOf("accountable"));
        Assert.assertEquals("role", "HAS UNIT IN DATA FIELD PhDStudentInfo.Supervisor", activity.getOne("accountable").getBindingExpression());
    }
}
