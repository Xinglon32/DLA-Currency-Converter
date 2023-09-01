package uk.co.lettin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import uk.co.lettin.service.ConversionService;

@SpringBootApplication
public class DlaCurrencyApplication {

	/*
	 *	Spring Boot is probably overkill for this solution, however, it seemed the most related way to how DLA works to
	 * 	achieve the dependency injection I wanted to use for some other areas.
	 *
	 *  As a result, this class is more complicated than it should be for what is a one-and-done, top to bottom application.
	 *
	 *  I do generally find that SpringBoot is much harder to add after the fact than include to start, and context of
	 *  how this could be expanded has not been provided, where in a real world scenario, we would generally have some
	 *  idea what the future could hold for a solution.
	 *
	 *  High Level Notes:
	 *  - According to "Currency" - the class in Java that exists for the purposes of working with Currencies -
	 *      BigDecimal is the preferred type to use when working with them, so is used throughout
	 *  - Implementation of this started using "Currency", and then I realised that this class doesn't account for
	 * 		potential differences in its data and that provided by the CSV, so I opted for custom types
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DlaCurrencyApplication.class, args);
		ConversionService conversionService = context.getBean(ConversionService.class);
		conversionService.start();
	}

}
