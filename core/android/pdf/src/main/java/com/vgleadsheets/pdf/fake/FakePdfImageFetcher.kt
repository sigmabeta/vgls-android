package com.vgleadsheets.pdf.fake

//class FakePdfImageFetcher(
//    private val generator: FakePdfImageGenerator,
//    private val data: PdfConfigById,
//    private val options: Options
//): Fetcher {
//    override suspend fun fetch() = SourceFetchResult(
//        source = ImageSource(
//            file = pdfPath,
//            fileSystem = FileSystem.SYSTEM,
//            metadata = PdfMetadata(data.pageNumber)
//        ),
//        dataSource = DataSource.DISK,
//        mimeType = MIMETYPE
//    )
//
//    class Factory(
//        private val generator: FakePdfImageGenerator
//    ) : Fetcher.Factory<PdfConfigById> {
//        override fun create(
//            data: PdfConfigById,
//            options: Options,
//            imageLoader: ImageLoader
//        ) = FakePdfImageFetcher(generator, data, options)
//    }
//
//    companion object {
//        const val MIMETYPE = "application/pdf"
//    }
//}
