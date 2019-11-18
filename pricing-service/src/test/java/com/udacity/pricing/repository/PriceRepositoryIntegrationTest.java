package com.udacity.pricing.repository;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.udacity.pricing.entity.Price;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PriceRepositoryIntegrationTest {
    @Autowired
    private PriceRepository priceRepository;

    @Test
    public void shouldInsertDataIntoDatabase() {
        Optional<Price> priceFromRepo = priceRepository.findById(20L);
        assertFalse(priceFromRepo.isPresent());

        final Price createdPrice = priceRepository.save(new Price("BRL", new BigDecimal(20), 20L));
        priceFromRepo = priceRepository.findById(20L);
        assertTrue(priceFromRepo.isPresent());
        assertEquals(createdPrice.getVehicleId(), priceFromRepo.get().getVehicleId());
        assertEquals(createdPrice.getPrice(), new BigDecimal(20));
    }

    @Test
    public void shouldRetrieveOnePriceFromDatabase() {
        Optional<Price> priceFromRepo = priceRepository.findById(1L);
        assertTrue(priceFromRepo.isPresent());
    }

    @Test
    public void shouldRetrieveAllPricesFromDatabase() {
        final Iterable<Price> pricesIterator = priceRepository.findAll();
        assertEquals(pricesIterator.spliterator().estimateSize(), 19);
    }

    @Test
    public void shouldDeleteOnePriceFromDatabase() {
        Iterable<Price> pricesIterator = priceRepository.findAll();
        assertEquals(pricesIterator.spliterator().estimateSize(), 19);
        priceRepository.deleteById(1L);
        pricesIterator = priceRepository.findAll();
        assertEquals(pricesIterator.spliterator().estimateSize(), 18);
    }

    @Test
    public void shouldDeleteAllPricesFromDatabase() {
        Iterable<Price> pricesIterator = priceRepository.findAll();
        assertEquals(pricesIterator.spliterator().estimateSize(), 19);
        priceRepository.deleteAll();
        pricesIterator = priceRepository.findAll();
        assertEquals(pricesIterator.spliterator().estimateSize(), 0);
    }
}
