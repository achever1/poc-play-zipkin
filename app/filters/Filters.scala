package filters

import javax.inject.Inject
import jp.co.bizreach.trace.play.filter.ZipkinTraceFilter
import play.api.http.{DefaultHttpFilters, EnabledFilters}

class Filters @Inject()(
    defaultFilters: EnabledFilters,
    zipkinTraceFilter: ZipkinTraceFilter
) extends DefaultHttpFilters(
      zipkinTraceFilter)

