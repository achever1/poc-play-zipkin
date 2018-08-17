package filters

import javax.inject.Inject
import play.api.http.{DefaultHttpFilters, EnabledFilters}
import utils.ZipkinKafkaTraceFilter

class Filters @Inject()(
    defaultFilters: EnabledFilters,
    zipkinTraceFilter: ZipkinKafkaTraceFilter
) extends DefaultHttpFilters(zipkinTraceFilter)

