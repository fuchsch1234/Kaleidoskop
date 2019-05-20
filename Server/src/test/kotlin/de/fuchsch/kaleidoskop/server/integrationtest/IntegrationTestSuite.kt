import de.fuchsch.kaleidoskop.server.integrationtest.testrunner.ZerocodeSpringBootSuite
import org.jsmart.zerocode.core.domain.TargetEnv
import org.jsmart.zerocode.core.domain.TestPackageRoot
import org.junit.runner.RunWith

@TargetEnv("application.properties")
@TestPackageRoot("integrationtest")
@RunWith(ZerocodeSpringBootSuite::class)
class IntegrationTestSuite