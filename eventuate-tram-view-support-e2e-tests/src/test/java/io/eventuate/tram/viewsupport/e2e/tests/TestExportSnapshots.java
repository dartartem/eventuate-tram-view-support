package io.eventuate.tram.viewsupport.e2e.tests;

import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.util.test.async.Eventually;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EventuateTramViewSupportE2ETestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestExportSnapshots {

  @Autowired
  private TestDomainEntityRepository testDomainEntityRepository;

  @Autowired
  private TestDomainEntityViewRepository testDomainEntityViewRepository;

  @Autowired
  private DomainSnapshotExportService<TestDomainEntity> domainEntityDomainSnapshotExportService;

  @Test
  public void testThatViewsMatchOriginalEntitiesAfterExport() {
    String dataPrefix = UUID.randomUUID().toString();
    int domainEntitiesCount = 10;

    Set<String> originalData = new HashSet<>();

    for (int i = 0; i < domainEntitiesCount; i++) {
      String data = dataPrefix + i;
      testDomainEntityRepository.save(new TestDomainEntity(data));
      originalData.add(data);
    }

    domainEntityDomainSnapshotExportService.exportSnapshots();

    Eventually.eventually(() -> {
      List<TestDomainEntityView> views = testDomainEntityViewRepository.findAll();
      Set<String> viewData = views.stream().map(TestDomainEntityView::getData).collect(Collectors.toSet());
      Assert.assertEquals(originalData, viewData);
    });
  }

}
