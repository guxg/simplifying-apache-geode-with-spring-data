/*
 *  Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package example.app.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

import example.app.client.model.Customer;
import example.app.client.repo.CustomerRepository;

/**
 * The SpringBootApacheGeodeClientApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = Customer.class)
@EnableClusterConfiguration
public class SpringBootApacheGeodeClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootApacheGeodeClientApplication.class, args);
  }

  @Bean
  @SuppressWarnings("unused")
  ApplicationRunner runner(CustomerRepository customerRepository) {

    return args -> {

      assertThat(customerRepository.count()).isEqualTo(0);

      Customer jonDoe = Customer.newCustomer(1L, "Jon Doe");

      System.err.printf("Saving Customer [%s]...%n", jonDoe);

      jonDoe = customerRepository.save(jonDoe);

      assertThat(jonDoe).isNotNull();
      assertThat(jonDoe.getId()).isEqualTo(1L);
      assertThat(jonDoe.getName()).isEqualTo("Jon Doe");
      assertThat(customerRepository.count()).isEqualTo(1);

      System.err.println("Querying for Customer [SELECT * FROM /Customers WHERE name LIKE '%Doe']...");

      Customer queriedJonDoe = customerRepository.findByNameLike("%Doe");

      assertThat(queriedJonDoe).isEqualTo(jonDoe);

      System.err.printf("Customer was [%s]%n", queriedJonDoe);
    };
  }
}
