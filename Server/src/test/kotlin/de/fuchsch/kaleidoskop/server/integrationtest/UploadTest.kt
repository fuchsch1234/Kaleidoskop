package de.fuchsch.kaleidoskop.server.integrationtest

import de.fuchsch.kaleidoskop.server.integrationtest.testrunner.ZerocodeSpringBootRunner
import org.jsmart.zerocode.core.domain.JsonTestCase
import org.jsmart.zerocode.core.domain.TargetEnv
import org.junit.Test
import org.junit.runner.RunWith

@TargetEnv("application.properties")
@RunWith(ZerocodeSpringBootRunner::class)
class UploadTest {

    @Test
    @JsonTestCase("integrationtest/upload.json")
    fun `upload file`() { }

    @Test
    @JsonTestCase("integrationtest/upload_same_file.json")
    fun `uploading file with same name fails`() { }
}